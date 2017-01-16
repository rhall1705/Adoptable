package personal.rowan.petfinder.util

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

}