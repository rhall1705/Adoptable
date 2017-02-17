package personal.rowan.petfinder.ui.pet.master.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.pet.master.PetMasterViewModel
import rx.Subscription
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetMasterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val clickContainer: LinearLayout by bindView(R.id.pet_master_click_container)
    val photoContainer: FrameLayout by bindView(R.id.pet_master_photo_container)
    val textContainer: LinearLayout by bindView(R.id.pet_master_text_container)
    val photoView: ImageView by bindView(R.id.pet_master_photo)
    val fadeView: View by bindView(R.id.pet_master_fade_view)
    val nameView: TextView by bindView(R.id.pet_master_name)
    val headerView: TextView by bindView(R.id.pet_master_header)
    val detailView: TextView by bindView(R.id.pet_master_detail)

    private var clickSubscription: Subscription? = null

    fun bind(pet: Pet, clickSubject: PublishSubject<PetMasterClickData>) {
        val viewModel = PetMasterViewModel(clickContainer.context, pet)

        Picasso.with(photoView.context)
                .load(viewModel.photoUrl())
                .into(photoView)
        nameView.setText(viewModel.name())
        headerView.setText(viewModel.header())
        detailView.setText(viewModel.detail())

        if(clickSubscription != null && !clickSubscription!!.isUnsubscribed) {
            clickSubscription!!.unsubscribe()
        }

        val transitionViews: MutableList<android.support.v4.util.Pair<View, String>> = ArrayList()
        transitionViews.add(android.support.v4.util.Pair.create(photoView, photoView.context.getString(R.string.pet_master_detail_image_transition)))
        transitionViews.add(android.support.v4.util.Pair.create(fadeView, fadeView.context.getString(R.string.pet_master_detail_fade_transition)))
        transitionViews.add(android.support.v4.util.Pair.create(textContainer, textContainer.context.getString(R.string.pet_master_detail_text_transition)))

        clickSubscription = RxView.clicks(clickContainer).subscribe { v -> clickSubject.onNext(PetMasterClickData(pet, transitionViews)) }
    }

    class PetMasterClickData(pet: Pet, transitionViews: List<android.support.v4.util.Pair<View, String>>){
        private val mPet: Pet
        private val mTransitionViews: List<android.support.v4.util.Pair<View, String>>

        init {
            mPet = pet
            mTransitionViews = transitionViews
        }

        fun pet(): Pet {
            return mPet
        }

        fun transitionViews(): List<android.support.v4.util.Pair<View, String>> {
            return mTransitionViews
        }

    }

}