package personal.rowan.petfinder.ui.pet.master

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
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

    private var mPetList: MutableList<Pet>? = null
    private var mOffset: String = "0"
    private var mError: Throwable? = null

    fun loadData(type: Int?, arguments: PetMasterArguments) {
        mType = type
        mArguments = arguments
        loadData(false)
    }

    fun refreshData() {
        loadData(true)
    }

    private fun loadData(clear: Boolean) {
        if(isApiSubscriptionActive()) return

        if(clear) {
            mPetList?.clear()
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
                            if(mPetList == null) {
                                mPetList = ArrayList()
                            }

                            val pets: List<Pet>? = result!!.petfinder?.pets?.pet
                            if(pets != null) {
                                mPetList!!.addAll(pets)
                            }
                            val offset = result.petfinder!!.lastOffset?.`$t`
                            if(offset != null) {
                                mOffset = offset
                            }
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

    fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData) {
        mView.onPetClicked(petMasterClickData)
    }

    private fun isApiSubscriptionActive(): Boolean {
        return mApiSubscription != null && !mApiSubscription!!.isUnsubscribed
    }

    override fun publish() {
        if (mPetList != null) {
            mView.displayPets(mPetList!!)
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
        mPetList = null
        mError = null
    }

}
