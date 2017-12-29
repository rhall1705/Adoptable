package personal.rowan.petfinder.ui.shelter

/**
 * Created by Rowan Hall
 */
interface ShelterView {

    fun displayShelters(shelters: List<ShelterListViewState>)

    fun onPetsButtonClicked(pair: Pair<String?, String?>)

    fun onDirectionsButtonClicked(address: String)

    fun shouldPaginate(): Boolean

    fun showError(error: String)

    fun showProgress()

    fun hideProgress()

}