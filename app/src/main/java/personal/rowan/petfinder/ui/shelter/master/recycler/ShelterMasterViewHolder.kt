package personal.rowan.petfinder.ui.shelter.master.recycler

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
import rx.Subscription
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class ShelterMasterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)  {

    val titleView: TextView by bindView(R.id.shelter_master_title)
    val subtitleView: TextView by bindView(R.id.shelter_master_subtitle)
    val subtextView: TextView by bindView(R.id.shelter_master_subtext)
    val petsButton: Button by bindView(R.id.shelter_master_pets_button)
    val directionsButton: Button by bindView(R.id.shelter_master_directions_button)

    private var mPetsButtonSubscription: Subscription? = null
    private var mDirectionsButtonSubscription: Subscription? = null

    fun bind(shelter: Shelter, petsButtonSubject: PublishSubject<Shelter>, directionsButtonSubject: PublishSubject<Shelter>) {
        titleView.text = shelter.name!!.`$t`
        subtitleView.text = subtitleView.context.getString(R.string.shelter_master_subtitle, shelter.city!!.`$t`, shelter.state!!.`$t`, shelter.zip!!.`$t`)

        val subtextBuilder: StringBuilder = StringBuilder()
        val address1: String? = shelter.address1?.`$t`
        if (!TextUtils.isEmpty(address1)) {
            subtextBuilder.append(address1).append("\n")
        }
        val address2: String? = shelter.address2?.`$t`
        if (!TextUtils.isEmpty(address2)) {
            subtextBuilder.append(address2).append("\n")
        }
        val subtextContext: Context = subtitleView.context
        val phone: String? = shelter.phone?.`$t`
        if (!TextUtils.isEmpty(phone)) {
            subtextBuilder.append(subtextContext.getString(R.string.shelter_master_phone, phone)).append("\n")
        }
        val fax: String? = shelter.fax?.`$t`
        if (!TextUtils.isEmpty(fax)) {
            subtextBuilder.append(subtextContext.getString(R.string.shelter_master_fax, fax)).append("\n")
        }
        val email: String? = shelter.email?.`$t`
        if (!TextUtils.isEmpty(email)) {
            subtextBuilder.append(subtextContext.getString(R.string.shelter_master_email, email)).append("\n")
        }
        var subtextString: String = subtextBuilder.toString()
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