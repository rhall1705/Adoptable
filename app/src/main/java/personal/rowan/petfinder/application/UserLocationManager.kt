package personal.rowan.petfinder.application

import android.content.Context
import android.location.*
import rx.Observable
import rx.Subscription
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
    }

    private val mPermissionSubject: PublishSubject<Boolean> = PublishSubject.create()
    private val mZipcodeSubject: PublishSubject<String> = PublishSubject.create()
    private var mZipcodeSubscription: Subscription? = null

    fun permissionEvent(granted: Boolean) {
        mPermissionSubject.onNext(granted)
    }

    fun permissionObservable(): Observable<Boolean> {
        return mPermissionSubject
    }

    fun zipcodeObservable(): Observable<String> {
        return mZipcodeSubject
    }

    fun getZipcode(context: Context) {
        if (mZipcodeSubscription != null && !mZipcodeSubscription!!.isUnsubscribed) {
            return
        }

        mZipcodeSubscription = Observable.just(true)
                .map {
                    var zipcode: String
                    try {
                        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val location: Location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), false))
                        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
                        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        zipcode = addresses.get(0).postalCode
                    } catch (ignored: Exception) {
                        // todo: fallback to prefs
                        zipcode = "30308"
                    }
                    zipcode
                }
                .subscribe { zipcode -> mZipcodeSubject.onNext(zipcode) }


    }

}
