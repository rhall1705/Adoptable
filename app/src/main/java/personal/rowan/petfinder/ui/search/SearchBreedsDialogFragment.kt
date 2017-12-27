package personal.rowan.petfinder.ui.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Breeds
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class SearchBreedsDialogFragment : DialogFragment() {

    companion object {

        private val ARG_BREED_LIST = "SearchBreedsDialog.Arg.BreedList"

        fun getInstance(breeds: Breeds): SearchBreedsDialogFragment {
            val searchBreedsDialogFragment = SearchBreedsDialogFragment()
            val args = Bundle()
            val breedList = ArrayList<String>()
            for (breed in breeds.breed) {
                breedList.add(breed.`$t`!!)
            }
            args.putStringArrayList(ARG_BREED_LIST, breedList)
            searchBreedsDialogFragment.arguments = args
            return searchBreedsDialogFragment
        }

    }

    private val mBreedsSubject: PublishSubject<String> = PublishSubject.create()
    private lateinit var mBreeds: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBreeds = arguments!!.getStringArrayList(ARG_BREED_LIST)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity!!
        val view = activity.layoutInflater.inflate(R.layout.dialog_fragment_search_breeds, null)
        val recycler = view.findViewById<RecyclerView>(R.id.search_breed_recycler)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = SearchBreedsAdapter(mBreedsSubject, mBreeds)
        return AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(getString(R.string.search_breed_dialog_title))
                .create()
    }

    fun breedsObservable(): Observable<String> {
        return mBreedsSubject
    }

}