package personal.rowan.petfinder.ui.search

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchContainerActivity
import personal.rowan.petfinder.util.PetUtils
import rx.Subscription

/**
 * Created by Rowan Hall
 */
class SearchFragment : BaseFragment() {

    companion object {

        fun getInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val toolbar: Toolbar by bindView(R.id.search_toolbar)
    private val locationView: TextInputEditText by bindView(R.id.search_location)
    private val animalView: AppCompatSpinner by bindView(R.id.search_animal_spinner)
    private val sizeView: AppCompatSpinner by bindView(R.id.search_size_spinner)
    private val ageView: AppCompatSpinner by bindView(R.id.search_age_spinner)
    private val maleSexView: RadioButton by bindView(R.id.search_male_radio)
    private val searchFab: FloatingActionButton by bindView(R.id.search_fab)

    private var mSearchClickSubscription: Subscription? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar(toolbar, getString(R.string.search_title))
        val animalsAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.search_animal_options))
        animalsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        animalView.adapter = animalsAdapter
        val sizeAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.search_size_options))
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sizeView.adapter = sizeAdapter
        val ageAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.search_age_options))
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ageView.adapter = ageAdapter

        mSearchClickSubscription = RxView.clicks(searchFab).subscribe { performSearch() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(mSearchClickSubscription != null && !mSearchClickSubscription!!.isUnsubscribed) {
            mSearchClickSubscription!!.unsubscribe()
        }
    }

    private fun performSearch() {
        val location = locationView.text.toString()
        val animal = PetUtils.searchAnimalByIndex(animalView.selectedItemPosition)
        val size = PetUtils.searchSizeByIndex(sizeView.selectedItemPosition)
        val age = PetUtils.searchAgeByIndex(ageView.selectedItemPosition)
        val sex = if(maleSexView.isChecked) "M" else "F"
        val breed = null
        startActivity(PetMasterSearchContainerActivity.getIntent(context, location, animal, size, age, sex, breed))
    }

}