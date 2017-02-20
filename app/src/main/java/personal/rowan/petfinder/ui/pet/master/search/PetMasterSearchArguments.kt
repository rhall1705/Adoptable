package personal.rowan.petfinder.ui.pet.master.search

import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.ui.pet.master.PetMasterArguments
import personal.rowan.petfinder.util.StringUtils

/**
 * Created by Rowan Hall
 */
class PetMasterSearchArguments @JvmOverloads constructor(private val mLocation: String,
                                           private val mAnimal: String? = "",
                                           private val mSize: String? = "",
                                           private val mAge: String? = "",
                                           private val mSex: String? = "",
                                           private val mBreed: String? = "") : PetMasterArguments {

    fun location(): String {
        return mLocation
    }

    fun animal(): String? {
        return StringUtils.nullIfEmpty(mAnimal)
    }

    fun size(): String? {
        return StringUtils.nullIfEmpty(mSize)
    }

    fun age(): String? {
        return StringUtils.nullIfEmpty(mAge)
    }

    fun sex(): String? {
        return StringUtils.nullIfEmpty(mSex)
    }

    fun breed(): String? {
        return StringUtils.nullIfEmpty(mBreed)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetMasterSearchArguments> = object : Parcelable.Creator<PetMasterSearchArguments> {
            override fun createFromParcel(source: Parcel): PetMasterSearchArguments = PetMasterSearchArguments(source)
            override fun newArray(size: Int): Array<PetMasterSearchArguments?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(mLocation)
        dest?.writeString(mAnimal)
        dest?.writeString(mSize)
        dest?.writeString(mAge)
        dest?.writeString(mSex)
        dest?.writeString(mBreed)
    }
}