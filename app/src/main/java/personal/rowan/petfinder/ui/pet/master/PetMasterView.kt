package personal.rowan.petfinder.ui.pet.master

import personal.rowan.petfinder.model.pet.Pet

/**
 * Created by Rowan Hall
 */
interface PetMasterView {

    fun displayPets(pets: List<Pet>)

    fun onPetClicked(pet: Pet)

    fun shouldPaginate(): Boolean

    fun showError(error: String)

    fun showProgress()

    fun hideProgress()

    fun showPagination()

    fun hidePagination()

}