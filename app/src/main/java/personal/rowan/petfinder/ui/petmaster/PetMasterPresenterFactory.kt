package personal.rowan.petfinder.ui.petmaster

import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.petmaster.dagger.PetMasterScope
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterPresenterFactory @Inject constructor(private val mPetfinderService: PetfinderService) : PresenterFactory<PetMasterPresenter> {

    override fun create(): PetMasterPresenter {
        return PetMasterPresenter(mPetfinderService)
    }

}