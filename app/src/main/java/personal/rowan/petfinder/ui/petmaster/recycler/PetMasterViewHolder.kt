package personal.rowan.petfinder.ui.petmaster.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.Photo
import personal.rowan.petfinder.util.PetUtils

/**
 * Created by Rowan Hall
 */
class PetMasterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val photoView: ImageView by bindView(R.id.pet_master_photo)
    val nameView: TextView by bindView(R.id.pet_master_name)
    val headerView: TextView by bindView(R.id.pet_master_header)
    val detailView: TextView by bindView(R.id.pet_master_detail)

    fun bind(pet: Pet) {
        val photoList: List<Photo>? = pet.media?.photos?.photo
        if(photoList != null && photoList.size > 2) {
            Picasso.with(photoView.context)
                    .load(photoList[2].`$t`)
                    .into(photoView)
        }
        nameView.setText(pet.name!!.`$t`)
        headerView.setText(headerView.context.getString(
                R.string.pet_master_header,
                pet.animal!!.`$t`,
                pet.breeds!!.breed.get(0).`$t`))
        detailView.setText(detailView.context.getString(
                R.string.pet_master_detail,
                PetUtils.formatSize(pet.size!!.`$t`!!),
                pet.age!!.`$t`,
                PetUtils.formatSex(pet.sex!!.`$t`!!),
                pet.contact!!.city!!.`$t`,
                pet.contact!!.state!!.`$t`
        ))
    }

}