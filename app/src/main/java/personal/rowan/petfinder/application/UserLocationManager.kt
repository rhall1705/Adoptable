package personal.rowan.petfinder.application

import android.content.Context
import android.location.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.*
import javax.inject.Singleton

/**
 * Created by Rowan Hall
 */

@Singleton
class UserLocationManager private constructor() {

    private object Holder { val INSTANCE = UserLocationManager() }

    companion object {
        val INSTANCE: UserLocationManager by lazy { Holder.INSTANCE }
        val ERROR = "ERROR"
    }

    private val mPermissionSubject: PublishSubject<Boolean> = PublishSubject.create()

    fun permissionEvent(granted: Boolean) {
        mPermissionSubject.onNext(granted)
    }

    fun permissionObservable(): Observable<Boolean> {
        return mPermissionSubject
    }

    fun zipcodeObservable(context: Context): Observable<String> {
        return Observable.fromCallable {
            var zipcode: String
            try {
                val appContext = context.applicationContext
                val locationManager: LocationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location: Location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), false))
                val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                zipcode = addresses.get(0).postalCode
            } catch (ignored: Exception) {
                zipcode = ERROR
            }
            zipcode
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}
