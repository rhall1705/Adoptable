package personal.rowan.petfinder.ui.pet.master.recycler

import android.os.Build
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.squareup.picasso.Picasso
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewModel
import personal.rowan.petfinder.ui.pet.master.PetMasterViewModel
import rx.Subscription
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class PetMasterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val clickContainer: LinearLayout by bindView(R.id.pet_master_click_container)
    val textContainer: LinearLayout by bindView(R.id.pet_master_text_container)
    val photoView: ImageView by bindView(R.id.pet_master_photo)
    val fadeView: View by bindView(R.id.pet_master_fade_view)
    val nameView: TextView by bindView(R.id.pet_master_name)
    val headerView: TextView by bindView(R.id.pet_master_header)
    val detailView: TextView by bindView(R.id.pet_master_detail)

    private var mClickSubscription: Subscription? = null

    fun bind(viewModel: PetMasterViewModel, clickSubject: PublishSubject<PetMasterClickData>) {
        val photoUrl = viewModel.photoUrl()
        if (!photoUrl.isBlank()) {
            Picasso.with(photoView.context)
                    .load(viewModel.photoUrl())
                    .into(photoView)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photoView.transitionName = photoUrl
            }
        }

        nameView.setText(viewModel.name())
        headerView.setText(viewModel.header())
        detailView.setText(viewModel.detail())

        if(mClickSubscription != null && !mClickSubscription!!.isUnsubscribed) {
            mClickSubscription!!.unsubscribe()
        }

        val transitionViews: Array<Pair<View, String>> = arrayOf(
                Pair.create(photoView as View, photoUrl),
                Pair.create(fadeView, fadeView.context.getString(R.string.pet_master_detail_fade_transition)),
                Pair.create(textContainer as View, textContainer.context.getString(R.string.pet_master_detail_text_transition)))

        mClickSubscription = RxView.clicks(clickContainer).subscribe { v -> clickSubject.onNext(PetMasterClickData(viewModel as PetDetailViewModel, transitionViews)) }
    }

    class PetMasterClickData(private val mViewModel: PetDetailViewModel, private val mTransitionViews: Array<Pair<View, String>>){

        fun viewModel(): PetDetailViewModel {
            return mViewModel
        }

        fun transitionViews(): Array<Pair<View, String>> {
            return mTransitionViews
        }

    }

}