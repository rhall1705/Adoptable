package personal.rowan.petfinder.ui.pet.detail.photo

import android.view.ViewGroup
import android.support.v4.view.PagerAdapter
import android.view.View
import android.content.Context
import android.os.Build
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosAdapter(private val mContext: Context, private val mPhotoUrls: List<String>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val photoView = ImageView(mContext)
        val url = mPhotoUrls.get(position)
        collection.addView(photoView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoView.transitionName = url
        }

        Picasso.with(photoView.context)
                .load(url)
                .into(photoView)

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