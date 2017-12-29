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

    fun toggleFavorite(viewState: PetDetailViewState): Boolean {
        if (isFavorite(viewState.id())) {
            mRealmManager.removeFromFavorites(viewState)
        } else {
            mRealmManager.addToFavorites(viewState)
        }
        return isFavorite(viewState.id())
    }

    override fun onDestroyed() {
        mRealmManager.close()
    }

}