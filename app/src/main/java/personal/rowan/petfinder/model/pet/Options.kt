package personal.rowan.petfinder.model.pet

import java.util.*

class Options {

    var option: List<Option> = ArrayList()

    constructor(optionArray: Array<Option>) {
        val optionList: MutableList<Option> = ArrayList()
        optionList.addAll(optionArray)
        this.option = optionList
    }

    constructor(optionObject: Option) {
        val optionList: MutableList<Option> = ArrayList()
        optionList.add(optionObject)
        this.option = optionList
    }

}
