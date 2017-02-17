package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.Photo
import personal.rowan.petfinder.util.PetUtils

/**
 * Created by Rowan Hall
 */
open class PetMasterViewModel : Parcelable {

    constructor(context: Context, pet: Pet) {
        val photoList: List<Photo>? = pet.media?.photos?.photo
        if (photoList != null && photoList.size > 2) {
            mPhotoUrl = photoList[2].`$t`
        } else {
            mPhotoUrl = null
        }

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
        if (mPhotoUrl == null) return "" else return mPhotoUrl
    }

    fun name(): String {
        if (mName == null) return "" else return mName
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
        dest?.writeString(mPhotoUrl)
        dest?.writeString(mName)
        dest?.writeString(mHeader)
        dest?.writeString(mDetail)
    }
}