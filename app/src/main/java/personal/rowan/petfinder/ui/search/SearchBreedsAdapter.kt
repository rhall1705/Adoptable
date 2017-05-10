package personal.rowan.petfinder.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import personal.rowan.petfinder.R
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class SearchBreedsAdapter(private var mData: List<String>) : RecyclerView.Adapter<SearchBreedsViewHolder>() {

    private val mBreedsSubject: PublishSubject<String> = PublishSubject.create()

    override fun onBindViewHolder(holder: SearchBreedsViewHolder?, position: Int) {
        holder!!.bind(mData.get(position), mBreedsSubject)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchBreedsViewHolder {
        return SearchBreedsViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.listitem_generic, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun breedsObservable(): Observable<String> {
        return mBreedsSubject
    }

}