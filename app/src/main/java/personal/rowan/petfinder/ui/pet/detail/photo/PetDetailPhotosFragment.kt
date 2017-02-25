package personal.rowan.petfinder.ui.pet.detail.photo

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosFragment : BaseFragment() {

    private val toolbar: Toolbar by bindView(R.id.pet_detail_photos_toolbar)
    private val photoPager: ViewPager by bindView(R.id.pet_detail_photos_pager)

    companion object {

        private val ARG_PET_DETAIL_PHOTO_URLS = "PetDetailFragment.Arg.PhotoUrls"

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

        setToolbar(toolbar, "", true)
        photoPager.adapter = PetDetailPhotosAdapter(context, arguments.getStringArrayList(ARG_PET_DETAIL_PHOTO_URLS))
    }

}