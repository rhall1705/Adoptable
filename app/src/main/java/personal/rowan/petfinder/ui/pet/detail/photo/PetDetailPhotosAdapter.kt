package personal.rowan.petfinder.ui.pet.detail.photo

import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.view.PagerAdapter
import android.view.View
import android.content.Context
import personal.rowan.petfinder.R

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosAdapter(private val mContext: Context, private val mPhotoUrls: List<String>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val photoView = PetDetailPhotosView(mContext)
        collection.addView(photoView)
        photoView.loadPhoto(mPhotoUrls.get(position))
        return photoView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return mPhotoUrls.size
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }

    override fun getPageTitle(position: Int): CharSequence {
        return ""
    }

}