package personal.rowan.petfinder.ui.shelter.master

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
class ShelterMasterPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<ShelterMasterView>(ShelterMasterView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()
    private var mApiSubscription: Subscription? = null

    private lateinit var mLocation: String
    private var mShelterList: MutableList<Shelter>? = null
    private var mOffset: String = "0"
    private var mError: Throwable? = null

    fun loadData(location: String) {
        mLocation = location
        loadData(false)
    }

    fun refreshData() {
        loadData(true)
    }

    private fun loadData(clear: Boolean) {
        if(isApiSubscriptionActive()) return

        if(clear) {
            mShelterList!!.clear()
            mOffset = "0"
        }

        mApiSubscription = mPetfinderService.getNearbyShelters(mLocation, mOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<ShelterResult>() {
                    override fun onError(e: Throwable?) {
                        mError = e
                        publish()
                    }

                    override fun onCompleted() {
                        mView.hideProgress()
                        mView.hidePagination()
                    }

                    override fun onNext(result: ShelterResult?) {
                        if(mShelterList == null) {
                            mShelterList = ArrayList()
                        }

                        mShelterList!!.addAll(result!!.petfinder!!.shelters!!.shelter!!)
                        mOffset = result.petfinder!!.lastOffset!!.`$t`!!
                        publish()
                    }
                }
                )

        mCompositeSubscription.add(mApiSubscription)
    }

    fun bindRecyclerView(observable: Observable<RecyclerViewScrollEvent>) {
        mCompositeSubscription.add(observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { scrollEvent ->
                    if (mView.shouldPaginate() && !isApiSubscriptionActive()) {
                        mView.showPagination()
                        loadData(false)
                    }
                })
    }

    fun onPetsClicked(shelter: Shelter) {
        mView.onPetsButtonClicked(shelter)
    }

    fun onDirectionsClicked(shelter: Shelter) {
        mView.onDirectionsButtonClicked(shelter)
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