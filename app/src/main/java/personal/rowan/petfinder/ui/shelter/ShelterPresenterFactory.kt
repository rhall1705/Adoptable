package personal.rowan.petfinder.ui.shelter

import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class ShelterPresenterFactory @Inject constructor(private val mPetfinderService: PetfinderService) : PresenterFactory<ShelterPresenter> {

    override fun create(): ShelterPresenter {
        return ShelterPresenter(mPetfinderService)
    }

}