package personal.rowan.petfinder.ui.shelter

import android.content.Context
import android.text.TextUtils
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.util.StringUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class ShelterViewModel(context: Context, shelter: Shelter) {

    private val mTitle: String?
    private val mSubtitle: String
    private val mSubtext: String

    private val mId: String?
    private val mAddress: String

    init {
        mTitle = shelter.name?.`$t`
        mSubtitle = context.getString(R.string.shelter_subtitle, shelter.city?.`$t`, shelter.state?.`$t`, shelter.zip?.`$t`)

        val address1 = shelter.address1?.`$t`
        val address2 = shelter.address2?.`$t`
        val subtextList: MutableList<String?> = ArrayList()
        subtextList.add(address1)
        subtextList.add(address2)
        val phone = shelter.phone?.`$t`
        if (!TextUtils.isEmpty(phone)) {
            subtextList.add(context.getString(R.string.shelter_phone, phone))
        }
        val fax = shelter.fax?.`$t`
        if (!TextUtils.isEmpty(fax)) {
            subtextList.add(context.getString(R.string.shelter_fax, fax))
        }
        val email = shelter.email?.`$t`
        if (!TextUtils.isEmpty(email)) {
            subtextList.add(context.getString(R.string.shelter_email, email))
        }
        mSubtext = StringUtils.separateWithDelimiter(subtextList, "\n")

        mId = shelter.id?.`$t`

        val addressList: MutableList<String?> = ArrayList()
        addressList.add(address1)
        addressList.add(address2)
        addressList.add(mSubtitle)
        mAddress = StringUtils.separateWithDelimiter(addressList, "\n")
    }

    fun title(): String? {
        return mTitle
    }

    fun subtitle(): String {
        return mSubtitle
    }

    fun subtext(): String {
        return mSubtext
    }

    fun id(): String? {
        return mId
    }

    fun address(): String {
        return mAddress
    }

}