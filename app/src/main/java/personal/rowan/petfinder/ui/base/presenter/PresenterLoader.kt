package personal.rowan.petfinder.ui.base.presenter

import android.content.Context
import android.support.v4.content.Loader

/**
 * Created by Rowan Hall
 */

internal class PresenterLoader<P : BasePresenter<*>>(context: Context, private val mPresenterFactory: PresenterFactory<P>) : Loader<P>(context) {

    private var mPresenter: P? = null

    override fun onStartLoading() {
        super.onStartLoading()

        if (mPresenter == null) {
            forceLoad()
        } else {
            deliverResult(mPresenter)
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()
        mPresenter = mPresenterFactory.create()

        deliverResult(mPresenter)
    }

    override fun onReset() {
        super.onReset()
        if (mPresenter != null) {
            mPresenter!!.onDestroyed()
            mPresenter = null
        }
    }

    companion object {

        val LOADER_ID = 101
    }

}
