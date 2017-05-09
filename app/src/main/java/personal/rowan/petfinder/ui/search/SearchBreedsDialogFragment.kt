package personal.rowan.petfinder.ui.search

import android.os.Bundle
import android.support.v4.app.DialogFragment
import personal.rowan.petfinder.model.pet.Breeds

/**
 * Created by Rowan Hall
 */
class SearchBreedsDialogFragment : DialogFragment() {

    companion object {

        private val ARG_BREED_LIST = "SearchBreedsDialog.Arg.BreedList"

        fun getInstance(breeds: Breeds): SearchBreedsDialogFragment {
            val searchBreedsDialogFragment: SearchBreedsDialogFragment = SearchBreedsDialogFragment()
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

}