package personal.rowan.petfinder.ui.shelter.master

import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class ShelterMasterPresenterFactory @Inject constructor(private val mPetfinderService: PetfinderService) : PresenterFactory<ShelterMasterPresenter> {

    override fun create(): ShelterMasterPresenter {
        return ShelterMasterPresenter(mPetfinderService)
    }

}