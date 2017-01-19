package personal.rowan.petfinder.ui.shelter.master.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.shelter.Shelter
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class ShelterMasterAdapter(private var mData: List<Shelter>?) : RecyclerView.Adapter<ShelterMasterViewHolder>() {

    private var mPetsButtonSubject: PublishSubject<Shelter>
    private var mDirectionsButtonSubject: PublishSubject<Shelter>

    init {
        mPetsButtonSubject = PublishSubject.create()
        mDirectionsButtonSubject = PublishSubject.create()
    }

    fun paginateData(data: List<Shelter>) {
        if(mData == null || mData!!.isEmpty()) {
            mData = data
            notifyDataSetChanged()
        } else {
            val originalSize: Int = itemCount
            mData = data
            notifyItemRangeInserted(originalSize, data.size)
        }
    }

    override fun onBindViewHolder(holder: ShelterMasterViewHolder?, position: Int) {
        holder!!.bind(mData!!.get(position), mPetsButtonSubject, mDirectionsButtonSubject)
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ShelterMasterViewHolder {
        return ShelterMasterViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.listitem_shelter_master, parent, false))
    }

    fun petsButtonObservable(): Observable<Shelter> {
        return mPetsButtonSubject
    }

    fun directionsButtonObservable(): Observable<Shelter> {
        return mDirectionsButtonSubject
    }
    
}