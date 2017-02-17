package personal.rowan.petfinder.ui.pet.master

import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder

/**
 * Created by Rowan Hall
 */
interface PetMasterView {

    fun displayPets(pets: List<Pet>)

    fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData)

    fun shouldPaginate(): Boolean

    fun showError(error: String)

    fun showProgress()

    fun hideProgress()

    fun showPagination()

    fun hidePagination()

}