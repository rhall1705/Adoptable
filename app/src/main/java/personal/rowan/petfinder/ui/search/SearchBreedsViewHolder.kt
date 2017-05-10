package personal.rowan.petfinder.ui.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import personal.rowan.petfinder.R
import rx.Subscription
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class SearchBreedsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)  {

    val breedView: TextView by bindView(R.id.generic_listitem_text)

    private var mBreedSubscription: Subscription? = null

    fun bind(breedString: String, breedSubject: PublishSubject<String>) {
        breedView.text = breedString

        if (mBreedSubscription != null && !mBreedSubscription!!.isUnsubscribed) {
            mBreedSubscription!!.unsubscribe()
        }
        mBreedSubscription = RxView.clicks(breedView).subscribe { breedSubject.onNext(breedString) }
    }

}