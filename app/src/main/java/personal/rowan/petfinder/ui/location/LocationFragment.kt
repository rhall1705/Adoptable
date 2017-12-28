package personal.rowan.petfinder.ui.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import kotterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.location.dagger.LocationComponent
import personal.rowan.petfinder.ui.main.MainActivity
import personal.rowan.petfinder.util.AndroidUtils
import personal.rowan.petfinder.util.PermissionUtils
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class LocationFragment : BaseFragment() {

    companion object {

        fun getInstance(): LocationFragment {
            return LocationFragment()
        }
    }

    @Inject
    lateinit var mUserLocationManager: UserLocationManager

    private val locationFailureContainer: LinearLayout by bindView(R.id.location_failure_container)
    private val locationFailureButton: Button by bindView(R.id.location_failure_button)
    private val zipcodeEntry: EditText by bindView(R.id.zipcode_entry)
    private val zipcodeEntryButton: Button by bindView(R.id.zipcode_entry_button)
    private val locationPermissionContainer: LinearLayout by bindView(R.id.location_permission_container)
    private val locationPermissionButton: Button by bindView(R.id.location_permission_button)

    private val mCompositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationComponent.injector.call(this)

        mCompositeSubscription.add(mUserLocationManager.permissionObservable()
                .subscribe ({ enabled -> if(enabled) findZipcode() else showLocationPermission() },
                        { _ -> showLocationFailure() }))

        if(!PermissionUtils.hasPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
            handleLocationPermission()
            return
        }

        findZipcode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationPermissionButton.setOnClickListener { handleLocationPermission() }
        locationFailureButton.setOnClickListener { findZipcode() }
        zipcodeEntry.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                zipcodeEntryButton.isEnabled = !TextUtils.isEmpty(p0)
            }
        })
        zipcodeEntryButton.isEnabled = !TextUtils.isEmpty(zipcodeEntry.text)
        zipcodeEntryButton.setOnClickListener {
            proceedToMainActivity(zipcodeEntry.text.toString())
            AndroidUtils.hideKeyboard(context!!, zipcodeEntry)
        }
    }

    private fun showLocationPermission() {
        locationPermissionContainer.visibility = View.VISIBLE
        locationFailureContainer.visibility = View.GONE
    }

    private fun showLocationFailure() {
        locationPermissionContainer.visibility = View.GONE
        locationFailureContainer.visibility = View.VISIBLE
    }

    private fun handleLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionUtils.PERMISSION_CODE_LOCATION)
    }

    private fun findZipcode() {
        mCompositeSubscription.add(mUserLocationManager.zipcodeObservable(context!!)
                .subscribe { zipcode -> proceedToMainActivity(zipcode) })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_LOCATION ->
                mUserLocationManager.permissionEvent(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun proceedToMainActivity(zipcode: String) {
        if (TextUtils.isEmpty(zipcode) || zipcode == UserLocationManager.ERROR) {
            showLocationFailure()
            return
        }
        showToastMessage(getString(R.string.location_toast_message, zipcode))
        mUserLocationManager.saveZipcode(context!!, zipcode)
        startActivity(MainActivity.createIntent(context!!).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        activity?.finish()
    }

}