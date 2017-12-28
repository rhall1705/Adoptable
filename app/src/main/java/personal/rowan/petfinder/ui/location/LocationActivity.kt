package personal.rowan.petfinder.ui.location

import android.content.Context
import android.content.Intent
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.base.ContainerActivity

/**
 * Created by Rowan Hall
 */
class LocationActivity : ContainerActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, LocationActivity::class.java)
        }
    }

    override fun getFragment(): BaseFragment? {
        return LocationFragment.getInstance()
    }

}