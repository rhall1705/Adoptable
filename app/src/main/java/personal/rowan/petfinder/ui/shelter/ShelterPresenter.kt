package personal.rowan.petfinder.ui.shelter

import android.content.Context
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.model.shelter.ShelterResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * Created by Rowan Hall
 */
class ShelterPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<ShelterView>(ShelterView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()
    private var mApiSubscription: Subscription? = null

    private lateinit var mLocation: String
    private var mShelterList: MutableList<ShelterViewModel>? = null
    private var mOffset = "0"
    private var mError: Throwable? = null

    fun loadData(context: Context, location: String) {
        mLocation = location
        if (mShelterList == null) {
            loadData(context, false)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if(isApiSubscriptionActive()) return

        if(clear) {
            mShelterList?.clear()
            mOffset = "0"
        }

        mApiSubscription = mPetfinderService.getNearbyShelters(mLocation, mOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<ShelterResult>() {
                    override fun onError(e: Throwable?) {
                        mError = e
                        publish()
                        mView.hideProgress()
                        mView.hidePagination()
                    }

                    override fun onCompleted() {
                        mView.hideProgress()
                        mView.hidePagination()
                    }

                    override fun onNext(result: ShelterResult?) {
                        if(mShelterList == null) {
                            mShelterList = ArrayList()
                        }

                        val shelters: List<Shelter>? = result?.petfinder?.shelters?.shelter
                        if(shelters != null) {
                            val viewModels: MutableList<ShelterViewModel> = ArrayList()
                            for (shelter in shelters) {
                                viewModels.add(ShelterViewModel(context, shelter))
                            }
                            mShelterList!!.addAll(viewModels)
                        }
                        val offset = result?.petfinder!!.lastOffset?.`$t`
                        if(offset != null) {
                            mOffset = offset
                        }
                        publish()
                    }
                }
                )

        mCompositeSubscription.add(mApiSubscription)
    }

    fun bindRecyclerView(context: Context, observable: Observable<RecyclerViewScrollEvent>) {
        mCompositeSubscription.add(observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mView.shouldPaginate() && !isApiSubscriptionActive()) {
                        mView.showPagination()
                        loadData(context, false)
                    }
                })
    }

    fun onPetsClicked(pair: Pair<String?, String?>) {
        mView.onPetsButtonClicked(pair)
    }

    fun onDirectionsClicked(address: String) {
        mView.onDirectionsButtonClicked(address)
    }

    private fun isApiSubscriptionActive(): Boolean {
        return mApiSubscription != null && !mApiSubscription!!.isUnsubscribed
    }

    override fun publish() {
        if (mShelterList != null) {
            mView.displayShelters(mShelterList!!)
        } else if (mError != null) {
            mView.showError(mError.toString())
        } else {
            mView.showProgress()
        }
    }

    override fun onDestroyed() {
        if(!mCompositeSubscription.isUnsubscribed) {
            mCompositeSubscription.unsubscribe()
        }
        mApiSubscription = null
        mShelterList = null
        mError = null
    }

}