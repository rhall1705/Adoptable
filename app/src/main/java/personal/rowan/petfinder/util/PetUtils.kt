package personal.rowan.petfinder.util

/**
 * Created by Rowan Hall
 */
object PetUtils {

    val ANIMAL_OPTION_DOG = "dog"
    val ANIMAL_OPTION_CAT = "cat"
    val ANIMAL_OPTION_BIRD = "bird"
    val ANIMAL_OPTION_REPTILE = "reptile"
    val ANIMAL_OPTION_SMALL_FURRY = "smallfurry"
    val ANIMAL_OPTION_HORSE = "horse"
    val ANIMAL_OPTION_PIG = "pig"
    val ANIMAL_OPTION_BARNYARD = "barnyard"

    fun formatSize(size: String?): String {
        when(size) {
            "S" -> return "Small"
            "M" -> return "Medium"
            "L" -> return "Large"
            "XL" -> return "Extra Large"
            null -> return ""
            else -> return size
        }
    }

    fun formatSex(sex: String?): String {
        when(sex) {
            "M" -> return "Male"
            "F" -> return "Female"
            null -> return ""
            else -> return sex
        }
    }

    fun searchAnimalByIndex(index: Int): String? {
        when(index) {
            1 -> return ANIMAL_OPTION_DOG
            2 -> return ANIMAL_OPTION_CAT
            3 -> return ANIMAL_OPTION_BIRD
            4 -> return ANIMAL_OPTION_REPTILE
            5 -> return ANIMAL_OPTION_SMALL_FURRY
            6 -> return ANIMAL_OPTION_HORSE
            7 -> return ANIMAL_OPTION_PIG
            8 -> return ANIMAL_OPTION_BARNYARD
            else -> return null
        }
    }

    fun searchSizeByIndex(index: Int): String? {
        when(index) {
            1 -> return "S"
            2 -> return "M"
            3 -> return "L"
            4 -> return "XL"
            else -> return null
        }
    }

    fun searchAgeByIndex(index: Int): String? {
        when(index) {
            1 -> return "Baby"
            2 -> return "Young"
            3 -> return "Adult"
            4 -> return "Senior"
            else -> return null
        }
    }

}