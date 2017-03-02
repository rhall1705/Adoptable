package personal.rowan.petfinder.ui.pet.detail

import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import personal.rowan.petfinder.ui.pet.detail.dagger.PetDetailScope
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager

/**
 * Created by Rowan Hall
 */

@PetDetailScope
class PetDetailPresenter(private var mRealmManager: RealmFavoritesManager) : BasePresenter<PetDetailView>(PetDetailView::class.java) {

    fun isFavorite(petId: String): Boolean {
        return mRealmManager.isFavorite(petId)
    }

    fun toggleFavorite(viewModel: PetDetailViewModel): Boolean {
        if (isFavorite(viewModel.id())) {
            mRealmManager.removeFromFavorites(viewModel)
        } else {
            mRealmManager.addToFavorites(viewModel)
        }
        return isFavorite(viewModel.id())
    }

    override fun onDestroyed() {
        mRealmManager.close()
    }

}