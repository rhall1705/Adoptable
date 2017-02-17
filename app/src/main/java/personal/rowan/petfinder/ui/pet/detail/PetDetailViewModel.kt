package personal.rowan.petfinder.ui.pet.detail

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.pet.master.PetMasterViewModel

/**
 * Created by Rowan Hall
 */
class PetDetailViewModel: PetMasterViewModel, Parcelable {

    constructor(context: Context, pet: Pet): super(context, pet) {
        mDescription = pet.description?.`$t`
    }

    constructor(photoUrl: String?, name: String?, header: String, detail: String, description: String):
            super(photoUrl, name, header, detail) {
        mDescription = description
    }

    private val mDescription: String?

    fun description(): String {
        if (mDescription == null) return "" else return mDescription
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetDetailViewModel> = object : Parcelable.Creator<PetDetailViewModel> {
            override fun createFromParcel(source: Parcel): PetDetailViewModel = PetDetailViewModel(source)
            override fun newArray(size: Int): Array<PetDetailViewModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(photoUrl())
        dest?.writeString(name())
        dest?.writeString(header())
        dest?.writeString(detail())
        dest?.writeString(description())
    }
}