package personal.rowan.petfinder.ui.base.presenter

import android.support.annotation.CallSuper
import android.support.annotation.VisibleForTesting

import personal.rowan.petfinder.util.NullObject

/**
 * Created by Rowan Hall
 */

abstract class BasePresenter<V: Any>(private val mViewClazz: Class<V>) {

    protected lateinit var mView: V

    @CallSuper
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    open fun attach(view: V) {
        mView = view
        publish()
    }

    internal fun detach() {
        this.mView = NullObject.create(mViewClazz)
    }

    // Overridden in subclasses to be performed after the presenter is attached
    // Also called in subclasses following data being fetched
    open protected fun publish() {

    }

    // Overriden in subclasses
    open fun onDestroyed() {

    }

}
