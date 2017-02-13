package personal.rowan.petfinder.ui.pet.detail

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
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

        val viewModel: PetDetailViewModel = arguments.getParcelable(ARG_PET_DETAIL_MODEL)
        setToolbar(toolbar, viewModel.name(), true)
        Picasso.with(context)
                .load(viewModel.photoUrl())
                .into(photoView)
        headerView.setText(viewModel.header())
        detailView.setText(viewModel.detail())
    }

}