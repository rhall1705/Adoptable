package personal.rowan.petfinder.ui.pet.detail

import android.content.Intent
import android.net.Uri
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
        Picasso.with(context)
                .load(viewModel.photoUrl())
                .into(photoView)
        headerView.setText(viewModel.header())
        detailView.setText(viewModel.detail())

        handleDescription(viewModel.description())
        handlePhone(viewModel.phone())
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
        if (phone.trim().isEmpty()) {
            phoneDivider.visibility = View.GONE
            phoneView.visibility = View.GONE
        } else {
            phoneView.setText(phone)
            RxView.clicks(phoneView).subscribe { startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))) }
        }
    }

}