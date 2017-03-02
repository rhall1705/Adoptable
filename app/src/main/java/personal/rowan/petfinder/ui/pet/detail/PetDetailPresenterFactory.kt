package personal.rowan.petfinder.ui.pet.detail

import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.detail.dagger.PetDetailScope
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
@PetDetailScope
class PetDetailPresenterFactory @Inject constructor(private val mRealmManager: RealmFavoritesManager) : PresenterFactory<PetDetailPresenter> {

    override fun create(): PetDetailPresenter {
        return PetDetailPresenter(mRealmManager)
    }

}