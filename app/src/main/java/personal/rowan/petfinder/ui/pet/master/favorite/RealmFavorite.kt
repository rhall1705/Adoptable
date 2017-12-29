package personal.rowan.petfinder.ui.pet.master.favorite

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewState
import personal.rowan.petfinder.util.StringUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
open class RealmFavorite(@PrimaryKey private var mId: String,
                    private var mPhotoUrl: String,
                    private var mName: String,
                    private var mHeader: String,
                    private var mDetail: String,
                    private var mFavorite: Boolean,
                    private var mDescription: String,
                    private var mPhone: String,
                    private var mEmail: String,
                    private var mAddress: String,
                    private var mPhotos: RealmList<RealmString>): RealmObject() {

    constructor() : this("", "", "", "", "", false, "", "", "", "", RealmList())

    companion object {

        fun toRealm(viewState: PetDetailViewState): RealmFavorite {
            return RealmFavorite(viewState.id(),
                    viewState.photoUrl(),
                    viewState.name(),
                    viewState.header(),
                    viewState.detail(),
                    viewState.favorite(),
                    viewState.description(),
                    viewState.phone(),
                    viewState.email(),
                    viewState.address(),
                    RealmString.toRealmStringList(viewState.photos()))
        }

        fun toViewModel(favorites: List<RealmFavorite>): MutableList<PetDetailViewState> {
            val viewModels: MutableList<PetDetailViewState> = ArrayList()
            for(favorite in favorites) {
                viewModels.add(PetDetailViewState(favorite.id(),
                        favorite.photoUrl(),
                        favorite.name(),
                        favorite.header(),
                        favorite.detail(),
                        favorite.favorite(),
                        favorite.description(),
                        favorite.phone(),
                        favorite.email(),
                        favorite.address(),
                        RealmString.toStringList(favorite.photos())))
            }
            return viewModels
        }

    }

    fun id(): String {
        return StringUtils.emptyIfNull(mId)
    }

    fun photoUrl(): String {
        return StringUtils.emptyIfNull(mPhotoUrl)
    }

    fun name(): String {
        return StringUtils.emptyIfNull(mName)
    }

    fun header(): String {
        return mHeader
    }

    fun detail(): String {
        return mDetail
    }

    fun favorite(): Boolean {
        return mFavorite
    }

    fun description(): String {
        return StringUtils.emptyIfNull(mDescription)
    }

    fun phone(): String {
        return StringUtils.emptyIfNull(mPhone)
    }

    fun address(): String {
        return StringUtils.emptyIfNull(mAddress)
    }

    fun email(): String {
        return StringUtils.emptyIfNull(mEmail)
    }

    fun photos(): RealmList<RealmString> {
        return mPhotos
    }

}