package personal.rowan.petfinder.ui.search

import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class SearchPresenterFactory @Inject constructor(private val mPetfinderService: PetfinderService) : PresenterFactory<SearchPresenter> {

    override fun create(): SearchPresenter {
        return SearchPresenter(mPetfinderService)
    }

}