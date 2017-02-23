package personal.rowan.petfinder.ui.pet.detail

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.Photo
import personal.rowan.petfinder.ui.pet.master.PetMasterViewModel
import personal.rowan.petfinder.util.StringUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailViewModel: PetMasterViewModel, Parcelable {

    constructor(context: Context, pet: Pet): super(context, pet) {
        mDescription = pet.description?.`$t`

        val contact = pet.contact
        mPhone = contact?.phone?.`$t`
        mEmail = contact?.email?.`$t`

        val addressStrings: MutableList<String?> = ArrayList()
        addressStrings.add(contact?.address1?.`$t`)
        addressStrings.add(contact?.address2?.`$t`)
        addressStrings.add(context.getString(R.string.shelter_subtitle, contact?.city?.`$t`, contact?.state?.`$t`, contact?.zip?.`$t`))
        mAddress = StringUtils.separateWithDelimiter(addressStrings, "\n")

        val photoList: List<Photo>? = pet.media?.photos?.photo
        val photoUrlList: MutableList<String> = ArrayList()
        if (photoList != null && photoList.isNotEmpty()) {
            for(i in  0..photoList.size - 1) {
                if (i != 2) {
                    val photoUrl = photoList.get(i).`$t`
                    if (photoUrl != null) {
                        photoUrlList.add(photoUrl)
                    }
                }
            }
        }
        mPhotos = photoUrlList
    }

    constructor(photoUrl: String?, name: String?, header: String, detail: String, description: String, phone: String?, email: String?, address: String?):
            super(photoUrl, name, header, detail) {
        mDescription = description
        mPhone = phone
        mEmail = email
        mAddress = address
        mPhotos = ArrayList()
    }

    private val mDescription: String?
    private val mPhone: String?
    private val mEmail: String?
    private val mAddress: String?
    private val mPhotos: List<String>

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

    fun photos(): List<String> {
        return mPhotos
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetDetailViewModel> = object : Parcelable.Creator<PetDetailViewModel> {
            override fun createFromParcel(source: Parcel): PetDetailViewModel = PetDetailViewModel(source)
            override fun newArray(size: Int): Array<PetDetailViewModel?> = arrayOfNulls(size)
        }

        fun fromPetList(context: Context, pets: List<Pet>?): List<PetDetailViewModel> {
            val viewModels: MutableList<PetDetailViewModel> = ArrayList()
            if (pets != null) {
                for (pet in pets) {
                    viewModels.add(PetDetailViewModel(context, pet))
                }
            }
            return viewModels
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()) {
        source.readStringList(mPhotos)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeString(description())
        dest?.writeString(phone())
        dest?.writeString(email())
        dest?.writeString(address())
        dest?.writeStringList(photos())
    }
}