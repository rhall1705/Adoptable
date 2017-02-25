package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.util.PetUtils
import personal.rowan.petfinder.util.StringUtils

/**
 * Created by Rowan Hall
 */
open class PetMasterViewModel : Parcelable {

    constructor(context: Context, pet: Pet) {
        mPhotoUrl = PetUtils.findFirstLargePhotoUrl(pet.media?.photos?.photo)
        mName = pet.name?.`$t`
        mHeader = context.getString(
                R.string.pet_master_header,
                pet.animal?.`$t`,
                pet.breeds?.breed?.get(0)?.`$t`)
        mDetail = context.getString(
                R.string.pet_master_detail,
                PetUtils.formatSize(pet.size?.`$t`),
                pet.age!!.`$t`,
                PetUtils.formatSex(pet.sex?.`$t`),
                pet.contact?.city?.`$t`,
                pet.contact?.state?.`$t`)
    }

    constructor(photoUrl: String?, name: String?, header: String, detail: String) {
        mPhotoUrl = photoUrl
        mName = name
        mHeader = header
        mDetail = detail
    }

    private val mPhotoUrl: String?
    private val mName: String?
    private val mHeader: String
    private val mDetail: String

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

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetMasterViewModel> = object : Parcelable.Creator<PetMasterViewModel> {
            override fun createFromParcel(source: Parcel): PetMasterViewModel = PetMasterViewModel(source)
            override fun newArray(size: Int): Array<PetMasterViewModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(photoUrl())
        dest?.writeString(name())
        dest?.writeString(header())
        dest?.writeString(detail())
    }
}