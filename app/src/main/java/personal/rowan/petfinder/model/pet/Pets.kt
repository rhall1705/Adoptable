package personal.rowan.petfinder.model.pet

import java.util.*

class Pets {

    var pet: List<Pet> = ArrayList()

    constructor(petArray: Array<Pet>) {
        val petList: MutableList<Pet> = ArrayList()
        petList.addAll(petArray)
        this.pet = petList
    }

    constructor(petObject: Pet) {
        val petList: MutableList<Pet> = ArrayList()
        petList.add(petObject)
        this.pet = petList
    }

}
