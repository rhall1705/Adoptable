package personal.rowan.petfinder.ui.shelter.master

import personal.rowan.petfinder.model.shelter.Shelter

/**
 * Created by Rowan Hall
 */
interface ShelterMasterView {

    fun displayShelters(shelters: List<Shelter>)

    fun onPetsButtonClicked(shelter: Shelter)

    fun onDirectionsButtonClicked(shelter: Shelter)

    fun shouldPaginate(): Boolean

    fun showError(error: String)

    fun showProgress()

    fun hideProgress()

    fun showPagination()

    fun hidePagination()

}