package personal.rowan.petfinder.ui.base.presenter

/**
 * Created by Rowan Hall
 */

/**
 * Creates a Presenter object.
 * @param <P> presenter type
</P> */
interface PresenterFactory<P : BasePresenter<*>> {
    fun create(): P
}
