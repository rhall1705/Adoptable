package personal.rowan.petfinder.ui.shelter

import android.content.Context
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by Rowan Hall
 */
class ShelterPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<ShelterView>(ShelterView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    private lateinit var mLocation: String
    private var shelterResource: Resource<ShelterViewState> = Resource.starting()

    fun initialLoad(context: Context, location: String) {
        mLocation = location
        if (!shelterResource.hasData()) {
            loadData(context, true)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if (shelterResource.progress) return

        mCompositeSubscription.add(mPetfinderService.getNearbyShelters(mLocation, offset())
                .map { Resource.success(ShelterViewState.fromShelterResult(if (shelterResource.hasData()) shelterResource.data() else null, it, clear, context)) }
                .startWith(Resource.progress(shelterResource))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            shelterResource = it!!
                            publish()
                        },
                        {
                            shelterResource = Resource.failure(shelterResource, it!!)
                            publish()
                        })
        )
    }

    fun bindRecyclerView(context: Context, observable: Observable<RecyclerViewScrollEvent>) {
        mCompositeSubscription.add(observable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mView != null && mView!!.shouldPaginate() && !shelterResource.progress) {
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
            view.showProgress()
        } else {
            view.hideProgress()
        }
        if (shelterResource.hasData()) {
            view.displayShelters(shelterResource.data().shelterData)
        }
        if (shelterResource.hasError()) {
            view.showError(shelterResource.error().toString())
        }
    }

    override fun onDestroyed() {
        if (!mCompositeSubscription.isUnsubscribed) {
            mCompositeSubscription.unsubscribe()
        }
    }

}