package personal.rowan.petfinder.util

/**
 * Created by Rowan Hall
 */
object StringUtils {

    fun emptyIfNull(string: String?): String {
        if (string == null) return "" else return string
    }

    fun separateWithDelimiter(strings: List<String>, delimiter: String): String {
        val stringBuilder = StringBuilder()
        for (i in 0..strings.size - 1) {
            stringBuilder.append(strings.get(i))
            if (i < strings.size) {
                stringBuilder.append(delimiter)
            }
        }
        return stringBuilder.toString()
    }

}