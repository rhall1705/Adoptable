package personal.rowan.petfinder.ui.pet.detail.photo

import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.jakewharton.rxbinding.support.v4.view.RxViewPager
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosFragment : BaseFragment() {

    private val toolbar: Toolbar by bindView(R.id.pet_detail_photos_toolbar)
    private val photoPager: ViewPager by bindView(R.id.pet_detail_photos_pager)
    private val sharedElementImage: ImageView by bindView(R.id.pet_detail_photos_shared_element)

    companion object {

        private val ARG_PET_DETAIL_PHOTO_URLS = "PetDetailPhotosFragment.Arg.PhotoUrls"

        fun getInstance(photoUrls: ArrayList<String>): PetDetailPhotosFragment {
            val fragment = PetDetailPhotosFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_PET_DETAIL_PHOTO_URLS, photoUrls)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_detail_photos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUrls = arguments.getStringArrayList(ARG_PET_DETAIL_PHOTO_URLS)
        val adapter = PetDetailPhotosAdapter(context, photoUrls)
        val maxCount = adapter.count
        RxViewPager.pageSelections(photoPager).subscribe { position -> toolbar.title = getTitle(position, maxCount) }
        photoPager.adapter = adapter
        setToolbar(toolbar, getTitle(0, maxCount), true)

        // Shared element transition seems to have an issue with ViewPager, so we use this fake view
        Picasso.with(context)
                .load(if (photoUrls.isNotEmpty()) photoUrls.get(0) else null)
                .into(sharedElementImage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoPager.visibility = View.INVISIBLE
            val sharedElementEnterTransition = activity.window.sharedElementEnterTransition
            sharedElementEnterTransition.addListener(object: Transition.TransitionListener {

                override fun onTransitionResume(transition: Transition) {
                }

                override fun onTransitionPause(transition: Transition) {
                }

                override fun onTransitionCancel(transition: Transition) {
                    photoPager.visibility = View.VISIBLE
                }

                override fun onTransitionStart(transition: Transition) {
                }

                override fun onTransitionEnd(transition: Transition) {
                    photoPager.visibility = View.VISIBLE
                }
            })
        } else {
            sharedElementImage.visibility = View.INVISIBLE
        }
    }

    private fun getTitle(position: Int, maxSize: Int): String {
        return getString(R.string.pet_detail_photo_title, (position + 1).toString(), maxSize.toString())
    }

}