package personal.rowan.petfinder.ui.base.presenter

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader

import personal.rowan.petfinder.ui.base.BaseFragment

/**
 * Created by Rowan Hall
 */

abstract class BasePresenterFragment<P : BasePresenter<V>, V: Any> : BaseFragment() {

    private var mPresenter: BasePresenter<V>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beforePresenterPrepared()
        loaderManager.initLoader(loaderId(), null, object : LoaderManager.LoaderCallbacks<P> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<P> {
                return PresenterLoader(context, presenterFactory)
            }

            override fun onLoadFinished(loader: Loader<P>, presenter: P) {
                mPresenter = presenter
                onPresenterPrepared(presenter)
            }

            override fun onLoaderReset(loader: Loader<P>) {
                mPresenter = null
                onPresenterDestroyed()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        mPresenter!!.attach(presenterView)
    }

    override fun onStop() {
        mPresenter!!.detach()
        super.onStop()
    }

    /**
     * Instance of [PresenterFactory] use to getINSTANCE a Presenter when needed. This INSTANCE should
     * not contain [android.app.Activity] context reference since it will be keep on rotations.
     */
    protected abstract val presenterFactory: PresenterFactory<P>

    /**
     * Hook for subclasses for before the [BasePresenter] is instantiated.
     * Primarily used to construct or inject dependencies for the Presenter.
     */
    open protected fun beforePresenterPrepared() {

    }

    /**
     * Hook for subclasses that deliver the [BasePresenter] before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    open protected fun onPresenterPrepared(presenter: P) {

    }

    /**
     * Hook for subclasses before the screen gets destroyed.
     */
    open protected fun onPresenterDestroyed() {

    }

    /**
     * Override in case of activity not implementing Presenter<View> interface
    </View> */
    @Suppress("UNCHECKED_CAST")
    protected val presenterView: V
        get() = this as V

    /**
     * Use this method in case you want to specify a specific ID for the [PresenterLoader].
     * By default its value would be PresenterLoader.LOADER_ID.
     */
    open protected fun loaderId(): Int {
        return PresenterLoader.LOADER_ID
    }

}
