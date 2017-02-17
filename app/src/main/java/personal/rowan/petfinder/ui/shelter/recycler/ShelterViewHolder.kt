package personal.rowan.petfinder.ui.shelter.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.util.StringUtils
import rx.Subscription
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by Rowan Hall
 */
class ShelterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)  {

    val titleView: TextView by bindView(R.id.shelter_title)
    val subtitleView: TextView by bindView(R.id.shelter_subtitle)
    val subtextView: TextView by bindView(R.id.shelter_subtext)
    val petsButton: Button by bindView(R.id.shelter_pets_button)
    val directionsButton: Button by bindView(R.id.shelter_directions_button)

    private var mPetsButtonSubscription: Subscription? = null
    private var mDirectionsButtonSubscription: Subscription? = null

    fun bind(shelter: Shelter, petsButtonSubject: PublishSubject<Shelter>, directionsButtonSubject: PublishSubject<Shelter>) {
        titleView.text = shelter.name!!.`$t`
        subtitleView.text = subtitleView.context.getString(R.string.shelter_subtitle, shelter.city!!.`$t`, shelter.state!!.`$t`, shelter.zip!!.`$t`)

        val subtextList: MutableList<String> = ArrayList()
        val address1: String? = shelter.address1?.`$t`
        if (!TextUtils.isEmpty(address1)) {
            subtextList.add(address1!!)
        }
        val address2: String? = shelter.address2?.`$t`
        if (!TextUtils.isEmpty(address2)) {
            subtextList.add(address2!!)
        }
        val subtextContext: Context = subtitleView.context
        val phone: String? = shelter.phone?.`$t`
        if (!TextUtils.isEmpty(phone)) {
            subtextList.add(subtextContext.getString(R.string.shelter_phone, phone))
        }
        val fax: String? = shelter.fax?.`$t`
        if (!TextUtils.isEmpty(fax)) {
            subtextList.add(subtextContext.getString(R.string.shelter_fax, fax))
        }
        val email: String? = shelter.email?.`$t`
        if (!TextUtils.isEmpty(email)) {
            subtextList.add(subtextContext.getString(R.string.shelter_email, email))
        }
        var subtextString: String = StringUtils.separateWithDelimiter(subtextList, "\n")
        if (!subtextString.isEmpty()) {
            subtextString = subtextString.substring(0, subtextString.length - 2)
        }
        subtextView.setText(subtextString)

        if (mPetsButtonSubscription != null && !mPetsButtonSubscription!!.isUnsubscribed) {
            mPetsButtonSubscription!!.unsubscribe()
        }
        mPetsButtonSubscription = RxView.clicks(petsButton).subscribe { petsButtonSubject.onNext(shelter) }

        if (mDirectionsButtonSubscription != null && !mDirectionsButtonSubscription!!.isUnsubscribed) {
            mDirectionsButtonSubscription!!.unsubscribe()
        }
        mDirectionsButtonSubscription = RxView.clicks(directionsButton).subscribe { directionsButtonSubject.onNext(shelter) }
    }

}