package personal.rowan.petfinder.ui.pet.master.favorite

import io.realm.Case
import io.realm.Realm
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewState
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
        val results = mRealm.where(RealmFavorite::class.java).beginsWith("mId", id, Case.INSENSITIVE).findAll()
        return results != null && results.size > 0
    }

    fun loadFavorites(): MutableList<PetDetailViewState> {
        return RealmFavorite.toViewModel(mRealm.where(RealmFavorite::class.java).findAll().asReversed())
    }

    fun addToFavorites(viewState: PetDetailViewState) {
        viewState.toggleFavorite(true)
        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(RealmFavorite.toRealm(viewState))
        mRealm.commitTransaction()
    }

    fun removeFromFavorites(viewState: PetDetailViewState) {
        viewState.toggleFavorite(false)
        mRealm.beginTransaction()
        mRealm.where(RealmFavorite::class.java).beginsWith("mId", viewState.id(), Case.INSENSITIVE).findAll().deleteAllFromRealm()
        mRealm.commitTransaction()
    }

    fun close() {
        mRealm.close()
    }

}