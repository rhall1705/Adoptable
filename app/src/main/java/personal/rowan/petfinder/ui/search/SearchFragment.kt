package personal.rowan.petfinder.ui.search

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Breeds
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchContainerActivity
import personal.rowan.petfinder.ui.search.dagger.SearchComponent
import personal.rowan.petfinder.util.PetUtils
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class SearchFragment : BasePresenterFragment<SearchPresenter, SearchView>(), SearchView {

    @Inject
    lateinit var mPresenterFactory: SearchPresenterFactory

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
    private val femaleSexView: RadioButton by bindView(R.id.search_female_radio)
    private val breedButton: Button by bindView(R.id.search_breed_button)
    private val searchFab: FloatingActionButton by bindView(R.id.search_fab)

    private lateinit var mPresenter: SearchPresenter

    override fun beforePresenterPrepared() {
        SearchComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar(toolbar, getString(R.string.search_title))
        animalView.adapter = generateSpinnerAdapter(R.array.search_animal_options)
        sizeView.adapter = generateSpinnerAdapter(R.array.search_size_options)
        ageView.adapter = generateSpinnerAdapter(R.array.search_age_options)
        RxView.clicks(breedButton).subscribe { mPresenter.loadBreeds(PetUtils.searchAnimalByIndex(animalView.selectedItemPosition)) }
        RxTextView.textChanges(locationView).subscribe { s -> searchFab.setEnabled(!s.isEmpty())}
        searchFab.setEnabled(false)

        RxView.clicks(searchFab).subscribe { performSearch() }
    }

    override fun onPresenterPrepared(presenter: SearchPresenter) {
        mPresenter = presenter
    }

    override fun displayBreedsProgress() {
        showProgressDialog(getString(R.string.search_breed_progress_title), getString(R.string.search_breed_progress_detail))
    }

    override fun hideBreedsProgress() {
        dismissProgressDialog()
    }

    override fun displayBreedsEmptyAnimalError() {
        showToastMessage(getString(R.string.search_breed_empty_animal_error))
    }

    override fun displayBreeds(breeds: Breeds) {
        SearchBreedsDialogFragment.getInstance(breeds).show(childFragmentManager, "tag")
    }

    override fun displayBreedsLoadingError(error: String) {
        showToastMessage(error)
    }

    private fun performSearch() {
        val location = locationView.text.toString()
        val animal = PetUtils.searchAnimalByIndex(animalView.selectedItemPosition)
        val size = PetUtils.searchSizeByIndex(sizeView.selectedItemPosition)
        val age = PetUtils.searchAgeByIndex(ageView.selectedItemPosition)
        val sex = if(maleSexView.isChecked) "M" else if(femaleSexView.isChecked) "F" else null
        val breed = null
        startActivity(PetMasterSearchContainerActivity.getIntent(context, location, animal, size, age, sex, breed))
    }

    private fun onBreedSelected(breed: String) {
        breedButton.setText(breed)
    }

    private fun generateSpinnerAdapter(@StringRes options: Int): ArrayAdapter<String> {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(options))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    override fun presenterFactory(): PresenterFactory<SearchPresenter> {
        return mPresenterFactory
    }

}