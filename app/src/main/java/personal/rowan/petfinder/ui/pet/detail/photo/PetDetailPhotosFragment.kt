package personal.rowan.petfinder.ui.pet.detail.photo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.view.*
import android.widget.ImageView
import kotterknife.bindView
import com.jakewharton.rxbinding.support.v4.view.RxViewPager
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.util.IntentUtils
import personal.rowan.petfinder.util.PermissionUtils
import personal.rowan.petfinder.util.PhotoUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosFragment : BaseFragment() {

    private val toolbar: Toolbar by bindView(R.id.pet_detail_photos_toolbar)
    private val photoPager: ViewPager by bindView(R.id.pet_detail_photos_pager)
    private val sharedElementImage: ImageView by bindView(R.id.pet_detail_photos_shared_element)
    private lateinit var mAdapter: PetDetailPhotosAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_detail_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUrls = arguments!!.getStringArrayList(ARG_PET_DETAIL_PHOTO_URLS)
        mAdapter = PetDetailPhotosAdapter(context!!, photoUrls)
        val maxCount = mAdapter.count
        RxViewPager.pageSelections(photoPager).subscribe { position -> toolbar.title = getTitle(position, maxCount) }
        photoPager.adapter = mAdapter
        setToolbar(toolbar, getTitle(0, maxCount), true)
        setHasOptionsMenu(true)

        // Shared element transition seems to have an issue with ViewPager, so we use this fake view
        Picasso.with(context)
                .load(if (photoUrls.isNotEmpty()) photoUrls.get(0) else null)
                .into(sharedElementImage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoPager.visibility = View.INVISIBLE
            val sharedElementEnterTransition = activity!!.window.sharedElementEnterTransition
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.pet_detail_photo_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_share_photo -> {
                // Always show the chooser as user may wish to share via a different app
                startActivity(Intent.createChooser(
                        IntentUtils.shareTextIntent(getUrlAtCurrentPosition()),
                        getString(R.string.pet_detail_photo_share_menu)))
                return true
            }
            R.id.action_download_photo -> {
                if (PermissionUtils.hasPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PhotoUtils.imageDownload(activity!!, getUrlAtCurrentPosition())
                } else {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PermissionUtils.PERMISSION_CODE_STORAGE)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_STORAGE ->
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.imageDownload(activity!!, getUrlAtCurrentPosition())
                }
        }
    }

    private fun getUrlAtCurrentPosition(): String {
        return mAdapter.urlAtPosition(photoPager.currentItem)
    }

    private fun getTitle(position: Int, maxSize: Int): String {
        return getString(R.string.pet_detail_photo_title, (position + 1).toString(), maxSize.toString())
    }

}