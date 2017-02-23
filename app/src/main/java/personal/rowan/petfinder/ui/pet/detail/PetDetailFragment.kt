package personal.rowan.petfinder.ui.pet.detail

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.util.IntentUtils

/**
 * Created by Rowan Hall
 */
class PetDetailFragment : BaseFragment() {

    private val toolbar: Toolbar by bindView(R.id.pet_detail_toolbar)
    private val photoView: ImageView by bindView(R.id.pet_detail_photo)
    private val headerView: TextView by bindView(R.id.pet_detail_header)
    private val detailView: TextView by bindView(R.id.pet_detail_detail)
    private val descriptionView: TextView by bindView(R.id.pet_detail_description)
    private val descriptionDivider: View by bindView(R.id.pet_detail_description_divider)
    private val phoneView: TextView by bindView(R.id.pet_detail_phone)
    private val phoneDivider: View by bindView(R.id.pet_detail_phone_divider)
    private val emailView: TextView by bindView(R.id.pet_detail_email)
    private val emailDivider: View by bindView(R.id.pet_detail_email_divider)
    private val addressView: TextView by bindView(R.id.pet_detail_address)
    private val addressDivider: View by bindView(R.id.pet_detail_address_divider)

    companion object {

        private val ARG_PET_DETAIL_MODEL = "PetDetailFragment.Arg.Model"

        fun getInstance(petDetailViewModel: PetDetailViewModel): PetDetailFragment {
            val fragment: PetDetailFragment = PetDetailFragment()
            val args: Bundle = Bundle()
            args.putParcelable(ARG_PET_DETAIL_MODEL, petDetailViewModel)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDetails(arguments.getParcelable(ARG_PET_DETAIL_MODEL))
    }

    private fun setDetails(viewModel: PetDetailViewModel) {
        setToolbar(toolbar, viewModel.name(), true)
        val photoUrl = viewModel.photoUrl()
        if (!photoUrl.isBlank()) {
            Picasso.with(context)
                    .load(viewModel.photoUrl())
                    .into(photoView)
        }
        headerView.setText(viewModel.header())
        detailView.setText(viewModel.detail())

        handleDescription(viewModel.description())
        handlePhone(viewModel.phone())
        handleEmail(viewModel.email())
        handleAddress(viewModel.address())
    }

    private fun handleDescription(description: String) {
        if (description.trim().isEmpty()) {
            descriptionDivider.visibility = View.GONE
            descriptionView.visibility = View.GONE
        } else {
            descriptionView.setText(description)
        }
    }

    private fun handlePhone(phone: String) {
        if (phone.isBlank()) {
            phoneDivider.visibility = View.GONE
            phoneView.visibility = View.GONE
        } else {
            phoneView.setText(phone)
            RxView.clicks(phoneView).subscribe { startActivity(IntentUtils.dialerIntent(phone)) }
        }
    }

    private fun handleEmail(email: String) {
        if (email.isBlank()) {
            emailDivider.visibility = View.GONE
            emailView.visibility = View.GONE
        } else {
            emailView.setText(email)
            RxView.clicks(emailView).subscribe { startActivity(IntentUtils.emailIntent(email)) }
        }
    }

    private fun handleAddress(address: String) {
        if (address.isBlank()) {
            addressDivider.visibility = View.GONE
            addressView.visibility = View.GONE
        } else {
            addressView.setText(address)
            RxView.clicks(addressView).subscribe { startActivity(IntentUtils.addressIntent(address)) }
        }
    }

}