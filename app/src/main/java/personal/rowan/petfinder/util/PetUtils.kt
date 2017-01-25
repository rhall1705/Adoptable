package personal.rowan.petfinder.util

import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
object PetUtils {

    fun formatSize(size: String): String {
        when(size) {
            "S" -> return "Small"
            "M" -> return "Medium"
            "L" -> return "Large"
            "XL" -> return "Extra Large"
            else -> return size
        }
    }

    fun formatSex(sex: String): String {
        when(sex) {
            "M" -> return "Male"
            "F" -> return "Female"
            else -> return sex
        }
    }

    fun searchAnimalByIndex(index: Int): String? {
        when(index) {
            0 -> return PetMasterFragment.ANIMAL_OPTION_DOG
            1 -> return PetMasterFragment.ANIMAL_OPTION_CAT
            2 -> return PetMasterFragment.ANIMAL_OPTION_BIRD
            3 -> return PetMasterFragment.ANIMAL_OPTION_REPTILE
            4 -> return PetMasterFragment.ANIMAL_OPTION_SMALL_FURRY
            5 -> return PetMasterFragment.ANIMAL_OPTION_HORSE
            6 -> return PetMasterFragment.ANIMAL_OPTION_PIG
            7 -> return PetMasterFragment.ANIMAL_OPTION_BARNYARD
            else -> return null
        }
    }

    fun searchSizeByIndex(index: Int): String? {
        when(index) {
            0 -> return "S"
            1 -> return "M"
            2 -> return "L"
            3 -> return "XL"
            else -> return null
        }
    }

    fun searchAgeByIndex(index: Int): String? {
        when(index) {
            0 -> return "Baby"
            1 -> return "Young"
            2 -> return "Adult"
            3 -> return "Senior"
            else -> return null
        }
    }

}