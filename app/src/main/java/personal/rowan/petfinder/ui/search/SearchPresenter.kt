package personal.rowan.petfinder.ui.search

import android.text.TextUtils
import personal.rowan.petfinder.model.pet.BreedListResult
import personal.rowan.petfinder.model.pet.Breeds
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by Rowan Hall
 */
class SearchPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<SearchView>(SearchView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    private var mAnimal: String? = null
    private var mResult: Breeds? = null
    private var mError: Throwable? = null

    fun loadBreeds(animal: String?) {
        if (TextUtils.isEmpty(animal)) {
            mView?.displayBreedsEmptyAnimalError()
            return
        }

        mAnimal = animal
        mResult = null
        mError = null
        mView?.displayBreedsProgress()
        mCompositeSubscription.add(mPetfinderService.getBreedList(mAnimal!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<BreedListResult>() {
                    override fun onCompleted() {
                        mView?.hideBreedsProgress()
                    }

                    override fun onError(e: Throwable?) {
                        mError = e
                        publish()
                    }

                    override fun onNext(t: BreedListResult?) {
                        mResult = t?.petfinder?.breeds
                        publish()
                    }
                })
        )
    }

    override fun publish() {
        if (mResult != null) {
            mView?.displayBreeds(mResult!!)
            mView?.hideBreedsProgress()
        } else if (mError != null) {
            mView?.displayBreedsLoadingError(mError!!.toString())
            mView?.hideBreedsProgress()
        } else if (mAnimal != null) {
            mView?.displayBreedsProgress()
        }

        mAnimal = null
        mResult = null
        mError = null
    }

}