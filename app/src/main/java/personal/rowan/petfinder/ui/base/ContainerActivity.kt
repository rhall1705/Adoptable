package personal.rowan.petfinder.ui.base

import android.os.Bundle
import android.widget.FrameLayout
import butterknife.bindView
import personal.rowan.petfinder.R

/**
 * Created by Rowan Hall
 */
abstract class ContainerActivity : BaseActivity() {

    val container: FrameLayout by bindView(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, getFragment())
                    .commit()
        }
    }

    abstract protected fun getFragment(): BaseFragment?

}