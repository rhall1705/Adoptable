package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchArguments
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterArguments
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterPresenter(private var mPetfinderService: PetfinderService, private var mRealmManager: RealmFavoritesManager) : BasePresenter<PetMasterView>(PetMasterView::class.java) {

    private val mCompositeSubscription = CompositeSubscription()

    private var mType: Int? = null
    private lateinit var mArguments: PetMasterArguments

    private var petResource: Resource<PetMasterViewState> = Resource.starting()

    fun initialLoad(context: Context, type: Int?, arguments: PetMasterArguments) {
        mType = type
        mArguments = arguments
        if (!petResource.hasData()) {
            loadData(context, true)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if(petResource.progress) return

        val petObservable: Observable<PetResult>
        when (mType) {
            PetMasterFragment.TYPE_FIND -> {
                val searchArgs: PetMasterSearchArguments = mArguments as PetMasterSearchArguments
                petObservable = mPetfinderService.getNearbyPets(searchArgs.location(), searchArgs.animal(), searchArgs.size(), searchArgs.age(), searchArgs.size(), searchArgs.breed(), offset())
            }
            PetMasterFragment.TYPE_SHELTER -> {
                val shelterArgs: PetMasterShelterArguments = mArguments as PetMasterShelterArguments
                petObservable = mPetfinderService.getPetsForShelter(shelterArgs.shelterId(), shelterArgs.status(), offset())
            }
            PetMasterFragment.TYPE_FAVORITE -> {
                petResource = Resource.success(PetMasterViewState(mRealmManager.loadFavorites(), "0"))
                publish()
                return
            }
            else -> throw RuntimeException("invalid pet master type")
        }
        mCompositeSubscription.add(petObservable
                .map { Resource.success(PetMasterViewState.fromPetResult(if (petResource.hasData()) petResource.data() else null, it, clear, context)) }
                .startWith(Resource.progress(petResource))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    petResource = it
                    publish()
                }, {
                    petResource = Resource.failure(petResource, it)
                    publish()
                })
        )
    }

    fun bindRecyclerView(context: Context, observable: Observable<RecyclerViewScrollEvent>) {
        if (mType != PetMasterFragment.TYPE_FAVORITE) {
            mCompositeSubscription.add(observable
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (mView != null && mView!!.shouldPaginate() && !petResource.progress) {
                            loadData(context, false)
                        }
                    })
        }
    }

    fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData) {
        mView?.onPetClicked(petMasterClickData)
    }

    fun onStart(context: Context) {
        if (mType == PetMasterFragment.TYPE_FAVORITE) {
            refreshData(context)
        }
    }

    private fun offset(): String {
        return if (petResource.hasData()) petResource.data().offset else "0"
    }

    override fun publish() {
        if (mView == null) {
            return
        }

        val view = mView!!
        if (petResource.progress) {
            view.showProgress()
        } else {
            view.hideProgress()
        }
        if (petResource.hasData()) {
            view.displayPets(petResource.data().petData, mType != PetMasterFragment.TYPE_FAVORITE)
        }
        if (petResource.hasError()) {
            view.showError(petResource.error().toString())
        }
    }

    override fun onDestroyed() {
        if(!mCompositeSubscription.isUnsubscribed) {
            mCompositeSubscription.unsubscribe()
        }
        mRealmManager.close()
    }

}
