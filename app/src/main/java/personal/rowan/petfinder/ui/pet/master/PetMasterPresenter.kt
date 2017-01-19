package personal.rowan.petfinder.ui.pet.master

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
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

    private var mLocation: String? = null
    private var mAnimal: String? = null

    private var mShelterId: String? = null
    private var mStatus: Char? = null

    private var mPetList: MutableList<Pet>? = null
    private var mOffset: String = "0"
    private var mError: Throwable? = null

    fun loadData(location: String, animal: String) {
        mType = PetMasterFragment.TYPE_FIND
        mLocation = location
        mAnimal = animal
        loadData(false)
    }

    fun loadData(shelterId: String, status: Char) {
        mType = PetMasterFragment.TYPE_SHELTER
        mShelterId = shelterId
        mStatus = status
        loadData(false)
    }

    fun refreshData() {
        loadData(true)
    }

    private fun loadData(clear: Boolean) {
        if(isApiSubscriptionActive()) return

        if(clear) {
            mPetList!!.clear()
            mOffset = "0"
        }

        val petObservable: Observable<PetResult>
        when (mType) {
            PetMasterFragment.TYPE_FIND -> petObservable = mPetfinderService.getNearbyPets(mLocation!!, mAnimal!!, mOffset)
            PetMasterFragment.TYPE_SHELTER -> petObservable = mPetfinderService.getPetsForShelter(mShelterId!!, mStatus!!, mOffset)
            else -> throw RuntimeException("invalid pet master type")
        }
        mApiSubscription = petObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<PetResult>() {
                        override fun onError(e: Throwable?) {
                            mError = e
                            publish()
                        }

                        override fun onCompleted() {
                            mView.hideProgress()
                            mView.hidePagination()
                        }

                        override fun onNext(result: PetResult?) {
                            if(mPetList == null) {
                                mPetList = ArrayList()
                            }

                            mPetList!!.addAll(result!!.petfinder!!.pets!!.pet)
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

    fun onPetClicked(pet: Pet) {
        mView.onPetClicked(pet)
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
