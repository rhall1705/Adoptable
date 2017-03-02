package personal.rowan.petfinder.ui.pet.master.favorite

import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.ui.pet.master.PetMasterArguments

/**
 * Created by Rowan Hall
 */
class PetMasterFavoriteArguments() : PetMasterArguments, Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetMasterFavoriteArguments> = object : Parcelable.Creator<PetMasterFavoriteArguments> {
            override fun createFromParcel(source: Parcel): PetMasterFavoriteArguments = PetMasterFavoriteArguments(source)
            override fun newArray(size: Int): Array<PetMasterFavoriteArguments?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {}
}