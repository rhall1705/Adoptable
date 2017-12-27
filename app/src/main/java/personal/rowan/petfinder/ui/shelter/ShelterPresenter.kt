package personal.rowan.petfinder.ui.shelter

import android.content.Context
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.model.shelter.ShelterResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import kotlin.collections.ArrayList

/**
 * Created by Rowan Hall
 */
class ShelterPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<ShelterView>(ShelterView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    private lateinit var mLocation: String
    private var mOffset = "0"
    private var shelterResource: Resource<MutableList<ShelterViewModel>> = Resource.starting()

    fun loadData(context: Context, location: String) {
        mLocation = location
        if (!shelterResource.hasData()) {
            loadData(context, false)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if (shelterResource.progress) return

        if (clear) {
            shelterResource = Resource.starting()
            mOffset = "0"
        }

        mCompositeSubscription.add(
                mPetfinderService.getNearbyShelters(mLocation, mOffset)
                        .map(object : Func1<ShelterResult, Resource<MutableList<ShelterViewModel>>> {
                            override fun call(result: ShelterResult?): Resource<MutableList<ShelterViewModel>> {
                                val shelterData: MutableList<ShelterViewModel> = ArrayList()
                                val shelters: List<Shelter>? = result?.petfinder?.shelters?.shelter
                                if (shelters != null) {
                                    val viewModels: MutableList<ShelterViewModel> = ArrayList()
                                    for (shelter in shelters) {
                                        viewModels.add(ShelterViewModel(context, shelter))
                                    }
                                    shelterData.addAll(viewModels)
                                }
                                val offset = result?.petfinder!!.lastOffset?.`$t`
                                if (offset != null) {
                                    mOffset = offset
                                }
                                return Resource.success(shelterData)
                            }
                        })
                        .startWith(Resource.progress(shelterResource))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Action1<Resource<MutableList<ShelterViewModel>>> {
                            override fun call(result: Resource<MutableList<ShelterViewModel>>?) {
                                shelterResource = result!!
                                publish()
                            }
                        }, object : Action1<Throwable> {
                            override fun call(t: Throwable?) {
                                shelterResource = Resource.failure(shelterResource, t!!)
                            }
                        }))
    }

    fun bindRecyclerView(context: Context, observable: Observable<RecyclerViewScrollEvent>) {
        mCompositeSubscription.add(observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mView != null && mView!!.shouldPaginate() && !shelterResource.progress) {
                        mView?.showPagination()
                        loadData(context, false)
                    }
                })
    }

    fun onPetsClicked(pair: Pair<String?, String?>) {
        mView?.onPetsButtonClicked(pair)
    }

    fun onDirectionsClicked(address: String) {
        mView?.onDirectionsButtonClicked(address)
    }

    override fun publish() {
        if (mView == null) {
            return
        }
        val view = mView!!
        if (shelterResource.progress) {
            view.showProgress()
        } else {
            view.hideProgress()
            view.hidePagination()
        }
        if (shelterResource.hasData()) {
            view.displayShelters(shelterResource.data())
        } else if (shelterResource.hasError()) {
            view.showError(shelterResource.error().toString())
        }
    }

    override fun onDestroyed() {
        if (!mCompositeSubscription.isUnsubscribed) {
            mCompositeSubscription.unsubscribe()
        }
    }

}