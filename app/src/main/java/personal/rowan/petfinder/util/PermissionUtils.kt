package personal.rowan.petfinder.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Created by Rowan Hall
 */
object PermissionUtils {

    val PERMISSION_CODE_LOCATION = 1

    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

}