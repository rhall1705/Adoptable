package personal.rowan.petfinder.ui.pet.master.favorite

import io.realm.Case
import io.realm.Realm
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewModel
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

class RealmFavoritesManager @Inject constructor(realm: Realm) {

    private val mRealm = realm

    fun isFavorite(id: String?): Boolean {
        if (id == null) {
            return false
        }
        return mRealm.where(RealmFavorite::class.java).beginsWith("mId", id, Case.INSENSITIVE) != null
    }

    fun loadFavorites(): List<PetDetailViewModel> {
        return RealmFavorite.toViewModel(mRealm.where(RealmFavorite::class.java).findAll().asReversed())
    }

    fun addToFavorites(viewModel: PetDetailViewModel) {
        viewModel.toggleFavorite(true)
        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(RealmFavorite.toRealm(viewModel))
        mRealm.commitTransaction()
    }

    fun removeFromFavorites(viewModel: PetDetailViewModel) {
        viewModel.toggleFavorite(false)
        mRealm.beginTransaction()
        mRealm.where(RealmFavorite::class.java).beginsWith("mId", viewModel.id(), Case.INSENSITIVE).findAll().deleteAllFromRealm()
    }

    fun close() {
        mRealm.close()
    }

}