package personal.rowan.petfinder.ui.base

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast

/**
 * Created by Rowan Hall
 */

abstract class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: ProgressDialog? = null

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressDialog(title: String, message: String) {
        mProgressDialog = ProgressDialog.show(this, title, message, true)
    }

    fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }

    @JvmOverloads fun setToolbar(toolbar: Toolbar, title: String, setUpButton: Boolean = false) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        setTitle(title)
        if (setUpButton && actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDefaultDisplayHomeAsUpEnabled(true)
        }
    }

    // Override this method for specific onUpPressed behavior
    protected fun onUpPressed() {
        onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onUpPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
