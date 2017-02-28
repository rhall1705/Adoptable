package personal.rowan.petfinder.ui.pet.detail.photo

import android.content.Context
import android.content.Intent
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.base.ContainerActivity
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosActivity : ContainerActivity() {

    companion object {
        private val ARG_PET_DETAIL_PHOTO_URLS = "PetDetailPhotosActivity.Extra.PhotoUrls"

        fun createIntent(context: Context, photoUrls: ArrayList<String>): Intent {
            val intent = Intent(context, PetDetailPhotosActivity::class.java)
            intent.putStringArrayListExtra(ARG_PET_DETAIL_PHOTO_URLS, photoUrls)
            return intent
        }
    }

    override fun getFragment(): BaseFragment? {
        return PetDetailPhotosFragment.getInstance(intent.getStringArrayListExtra(ARG_PET_DETAIL_PHOTO_URLS))
    }

}