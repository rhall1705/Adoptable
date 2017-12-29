package personal.rowan.petfinder.ui.pet.detail

import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding.view.RxView
import com.squareup.picasso.Picasso
import kotterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.detail.dagger.PetDetailComponent
import personal.rowan.petfinder.ui.pet.detail.photo.PetDetailPhotosActivity
import personal.rowan.petfinder.util.IntentUtils
import java.util.*
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class PetDetailFragment : BasePresenterFragment<PetDetailPresenter, PetDetailView>(), PetDetailView {

    @Inject
    lateinit var mPresenterFactory: PetDetailPresenterFactory

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
    private val favoriteFab: FloatingActionButton by bindView(R.id.pet_detail_favorite_fab)

    private lateinit var mPresenter: PetDetailPresenter

    companion object {

        private val ARG_PET_DETAIL_MODEL = "PetDetailFragment.Arg.Model"

        fun getInstance(petDetailViewState: PetDetailViewState): PetDetailFragment {
            val fragment = PetDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_PET_DETAIL_MODEL, petDetailViewState)
            fragment.arguments = args
            return fragment
        }

    }

    override fun beforePresenterPrepared() {
        PetDetailComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_detail, container, false)
    }

    override fun onPresenterPrepared(presenter: PetDetailPresenter) {
        mPresenter = presenter
        setDetails(arguments?.getParcelable(ARG_PET_DETAIL_MODEL))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.pet_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_photos -> {
                photoView.callOnClick()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setDetails(viewState: PetDetailViewState?) {
        if (viewState == null) {
            return
        }

        setToolbar(toolbar, viewState.name(), true)
        handlePhotos(viewState.photoUrl(), viewState.photos())
        headerView.setText(viewState.header())
        detailView.setText(viewState.detail())

        handleDescription(viewState.description())
        handlePhone(viewState.phone())
        handleEmail(viewState.email())
        handleAddress(viewState.address())
        handleFavoriteFab(viewState)
        setHasOptionsMenu(true)
    }

    private fun handlePhotos(photoUrl: String, allPhotos: List<String>) {
        if (!photoUrl.isBlank()) {
            val transition = getString(R.string.pet_detail_photo_transition)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photoView.transitionName = transition
            }

            Picasso.with(context)
                    .load(photoUrl)
                    .into(photoView)

            val allPhotosArrayList = ArrayList<String>()
            allPhotosArrayList.addAll(allPhotos)
            RxView.clicks(photoView).subscribe {
                startActivity(PetDetailPhotosActivity.createIntent(context!!, allPhotosArrayList),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, Pair.create(photoView, transition)).toBundle())
            }
        }
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

    private fun handleFavoriteFab(viewState: PetDetailViewState) {
        favoriteFab.setImageResource(if (mPresenter.isFavorite(viewState.id())) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border_white)
        RxView.clicks(favoriteFab).subscribe {
            favoriteFab.setImageResource(if (mPresenter.toggleFavorite(viewState)) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border_white)
        }
    }

    override fun presenterFactory(): PresenterFactory<PetDetailPresenter> {
        return mPresenterFactory
    }

}