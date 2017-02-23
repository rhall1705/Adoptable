package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewModel
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchArguments
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterArguments
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

@PetMasterScope
class PetMasterPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<PetMasterView>(PetMasterView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()
    private var mApiSubscription: Subscription? = null

    private var mType: Int? = null
    private lateinit var mArguments: PetMasterArguments

    private var mResults: MutableList<PetDetailViewModel>? = null
    private var mOffset: String = "0"
    private var mError: Throwable? = null

    fun loadData(context: Context, type: Int?, arguments: PetMasterArguments) {
        mType = type
        mArguments = arguments
        loadData(context, false)
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if(isApiSubscriptionActive()) return

        if(clear) {
            mResults?.clear()
            mOffset = "0"
        }

        val petObservable: Observable<PetResult>
        when (mType) {
            PetMasterFragment.TYPE_FIND -> {
                val searchArgs: PetMasterSearchArguments = mArguments as PetMasterSearchArguments
                petObservable = mPetfinderService.getNearbyPets(searchArgs.location(), searchArgs.animal(), searchArgs.size(), searchArgs.age(), searchArgs.size(), searchArgs.breed(), mOffset)
            }
            PetMasterFragment.TYPE_SHELTER -> {
                val shelterArgs: PetMasterShelterArguments = mArguments as PetMasterShelterArguments
                petObservable = mPetfinderService.getPetsForShelter(shelterArgs.shelterId(), shelterArgs.status(), mOffset)
            }
            else -> throw RuntimeException("invalid pet master type")
        }
        mApiSubscription = petObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<PetResult>() {
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

                        override fun onNext(result: PetResult?) {
                            if(mResults == null) {
                                mResults = ArrayList()
                            }

                            val pets: List<Pet>? = result?.petfinder?.pets?.pet
                            if(pets != null) {
                                mResults!!.addAll(PetDetailViewModel.fromPetList(context, pets))
                            }
                            val offset = result?.petfinder?.lastOffset?.`$t`
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
                .subscribe { scrollEvent ->
                    if (mView.shouldPaginate() && !isApiSubscriptionActive()) {
                        mView.showPagination()
                        loadData(context, false)
                    }
                })
    }

    fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData) {
        mView.onPetClicked(petMasterClickData)
    }

    private fun isApiSubscriptionActive(): Boolean {
        return mApiSubscription != null && !mApiSubscription!!.isUnsubscribed
    }

    override fun publish() {
        if (mResults != null) {
            mView.displayPets(mResults!!)
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
        mResults = null
        mError = null
    }

}
