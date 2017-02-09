package personal.rowan.petfinder.application

import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Singleton

/**
 * Created by Rowan Hall
 */

@Singleton
class LocationPermissionManager private constructor() {

    private object Holder { val INSTANCE = LocationPermissionManager() }

    companion object {
        val instance: LocationPermissionManager by lazy { Holder.INSTANCE }
    }

    private val permissionSubject: PublishSubject<Boolean> = PublishSubject.create()

    fun permissionEvent(granted: Boolean) {
        permissionSubject.onNext(granted)
    }

    fun permissionObservable(): Observable<Boolean> {
        return permissionSubject
    }

}
