package personal.rowan.petfinder.util

import android.content.Intent
import android.net.Uri

/**
 * Created by Rowan Hall
 */
object IntentUtils {

    fun dialerIntent(phone: String): Intent {
        return Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
    }

    fun addressIntent(address: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + address))
    }

    fun emailIntent(email: String): Intent {
        return Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto: " + email))
    }

    fun shareTextIntent(text: String): Intent {
        return Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, text).setType("text/plain")
    }

}