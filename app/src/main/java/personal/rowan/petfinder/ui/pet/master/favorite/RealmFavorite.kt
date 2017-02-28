package personal.rowan.petfinder.ui.pet.master.favorite

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewModel
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

        fun toRealm(viewModel: PetDetailViewModel): RealmFavorite {
            return RealmFavorite(viewModel.id(),
                    viewModel.photoUrl(),
                    viewModel.name(),
                    viewModel.header(),
                    viewModel.detail(),
                    viewModel.favorite(),
                    viewModel.description(),
                    viewModel.phone(),
                    viewModel.email(),
                    viewModel.address(),
                    RealmString.toRealmStringList(viewModel.photos()))
        }

        fun toViewModel(favorites: List<RealmFavorite>): List<PetDetailViewModel> {
            val viewModels: MutableList<PetDetailViewModel> = ArrayList()
            for(favorite in favorites) {
                viewModels.add(PetDetailViewModel(favorite.id(),
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