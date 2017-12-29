package personal.rowan.petfinder.ui.search

import android.text.TextUtils
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.model.pet.BreedListResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by Rowan Hall
 */
class SearchPresenter(private var mPetfinderService: PetfinderService) : BasePresenter<SearchView>(SearchView::class.java) {

    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()
    private var breedResource: Resource<BreedListResult> = Resource.starting()

    fun loadBreeds(animal: String?) {
        if (TextUtils.isEmpty(animal)) {
            mView?.displayBreedsEmptyAnimalError()
            return
        }

        mCompositeSubscription.add(mPetfinderService.getBreedList(animal!!)
                .map { Resource.success(it) }
                .startWith(Resource.progress(breedResource))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    breedResource = it
                    publish()
                }, {
                    breedResource = Resource.failure(breedResource, it)
                    publish()
                })
        )
    }

    override fun publish() {
        if (mView == null) {
            return
        }

        val view = mView!!
        if (breedResource.progress) {
            view.displayBreedsProgress()
        } else {
            view.hideBreedsProgress()
        }
        if (breedResource.hasData()) {
            val data = breedResource.data().petfinder?.breeds
            if (data != null) {
                view.displayBreeds(data)
            } else {
                view.displayBreedsLoadingError()
            }
            breedResource = Resource.starting()
        }
        if (breedResource.hasError()) {
            view.displayBreedsLoadingError()
            breedResource = Resource.starting()
        }
    }

}