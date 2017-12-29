package personal.rowan.petfinder.ui.search

import personal.rowan.petfinder.model.pet.Breeds

/**
 * Created by Rowan Hall
 */
interface SearchView {

    fun displayBreedsProgress()

    fun hideBreedsProgress()

    fun displayBreedsEmptyAnimalError()

    fun displayBreedsLoadingError()

    fun displayBreeds(breeds: Breeds)

}