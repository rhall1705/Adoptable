package personal.rowan.petfinder.ui.pet.master.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.pet.master.PetMasterViewModel
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class PetMasterAdapter(private var mData: List<PetMasterViewModel>?) : RecyclerView.Adapter<PetMasterViewHolder>() {

    private var mPetClickSubject: PublishSubject<PetMasterViewHolder.PetMasterClickData>

    init {
        mPetClickSubject = PublishSubject.create()
    }

    fun paginateData(data: List<PetMasterViewModel>) {
        if(mData == null || mData!!.isEmpty()) {
            mData = data
            notifyDataSetChanged()
        } else {
            val originalSize: Int = itemCount
            mData = data
            notifyItemRangeInserted(originalSize, data.size)
        }
    }

    override fun onBindViewHolder(holder: PetMasterViewHolder?, position: Int) {
        holder!!.bind(mData!!.get(position), mPetClickSubject)
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PetMasterViewHolder {
        return PetMasterViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.listitem_pet_master, parent, false))
    }

    fun itemClickObservable(): Observable<PetMasterViewHolder.PetMasterClickData> {
        return mPetClickSubject
    }

}