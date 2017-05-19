package personal.rowan.petfinder.ui.base

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar

/**
 * Created by Rowan Hall
 */

open class BaseFragment : Fragment() {

    protected val baseActivity: BaseActivity
        get() = activity as BaseActivity

    protected fun showToastMessage(message: String) {
        baseActivity.showToastMessage(message)
    }

    fun showProgressDialog(title: String, message: String) {
        baseActivity.showProgressDialog(title, message)
    }

    fun dismissProgressDialog() {
        baseActivity.dismissProgressDialog()
    }

    fun progressDialogShowing(): Boolean {
        return baseActivity.progressDialogShowing()
    }

    protected fun setToolbar(toolbar: Toolbar, title: String) {
        baseActivity.setToolbar(toolbar, title)
    }

    protected fun setToolbar(toolbar: Toolbar, title: String, setUpButton: Boolean) {
        baseActivity.setToolbar(toolbar, title, setUpButton)
    }

}
