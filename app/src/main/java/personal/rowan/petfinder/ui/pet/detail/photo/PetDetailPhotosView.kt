package personal.rowan.petfinder.ui.pet.detail.photo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.bindView
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R

/**
 * Created by Rowan Hall
 */
class PetDetailPhotosView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val photoView: ImageView by bindView(R.id.pet_detail_photos_item)

    constructor(context: Context?): this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pet_detail_photos_item, this, true)
    }

    fun loadPhoto(url: String) {
        Picasso.with(context)
                .load(url)
                .into(photoView)
    }

}