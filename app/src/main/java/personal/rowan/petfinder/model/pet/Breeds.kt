package personal.rowan.petfinder.model.pet

import java.util.*

class Breeds {

    var breed: List<Breed> = ArrayList()

    constructor(breedArray: Array<Breed>) {
        val breedList: MutableList<Breed> = ArrayList()
        breedList.addAll(breedArray)
        this.breed = breedList
    }

    constructor(breedObject: Breed) {
        val breedList: MutableList<Breed> = ArrayList()
        breedList.add(breedObject)
        this.breed = breedList
    }

}
