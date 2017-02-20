package personal.rowan.petfinder.util

import android.text.TextUtils

/**
 * Created by Rowan Hall
 */
object StringUtils {

    fun emptyIfNull(string: String?): String {
        return if (string == null) "" else string
    }

    fun nullIfEmpty(string: String?): String? {
        return if (string == null || string.isBlank()) null else string
    }

    fun separateWithDelimiter(strings: List<String?>, delimiter: String): String {
        val stringBuilder = StringBuilder()
        for (i in 0..strings.size - 1) {
            val string: String? = strings.get(i)
            if (!TextUtils.isEmpty(string) && !string.equals("null")) {
                stringBuilder.append(string)
                if (i < strings.size - 1) {
                    stringBuilder.append(delimiter)
                }
            }
        }
        return stringBuilder.toString()
    }

}