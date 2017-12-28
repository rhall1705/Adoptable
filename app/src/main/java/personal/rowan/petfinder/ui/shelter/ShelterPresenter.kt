package personal.rowan.petfinder.ui.shelter

import android.content.Context
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import kotlin.collections.ArrayList

/**
 * Created by Rowan Hall
 */
class ShelterPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<ShelterView>(ShelterView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    private lateinit var mLocation: String
    private var shelterResource: Resource<ShelterViewState> = Resource.starting()

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

        mCompositeSubscription.add(
                mPetfinderService.getNearbyShelters(mLocation, offset())
                        .map({
                                val shelterData: MutableList<ShelterListViewState> = if (clear && shelterResource.hasData()) shelterResource.data().shelterData else ArrayList()
                                val shelters: List<Shelter>? = it?.petfinder?.shelters?.shelter
                                if (shelters != null) {
                                    val listViewStates: MutableList<ShelterListViewState> = ArrayList()
                                    for (shelter in shelters) {
                                        listViewStates.add(ShelterListViewState(context, shelter))
                                    }
                                    shelterData.addAll(listViewStates)
                                }
                                var offset = it?.petfinder?.lastOffset?.`$t`
                                offset = if (offset == null) "0" else offset
                                Resource.success(ShelterViewState(shelterData, offset))
                            }
                        )
                        .startWith(Resource.progress(shelterResource, !clear))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Action1<Resource<ShelterViewState>> {
                            override fun call(result: Resource<ShelterViewState>?) {
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

    private fun offset(): String {
        return if (shelterResource.hasData()) shelterResource.data().offset else "0"
    }

    override fun publish() {
        if (mView == null) {
            return
        }
        val view = mView!!
        if (shelterResource.progress) {
            if (shelterResource.paginate) {
                view.showPagination()
            } else {
                view.showProgress()
            }
        } else {
            view.hideProgress()
            view.hidePagination()
        }
        if (shelterResource.hasData()) {
            view.displayShelters(shelterResource.data().shelterData)
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