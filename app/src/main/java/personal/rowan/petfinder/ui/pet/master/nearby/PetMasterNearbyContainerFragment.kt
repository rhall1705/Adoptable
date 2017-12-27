package personal.rowan.petfinder.ui.pet.master.nearby

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
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
import personal.rowan.petfinder.ui.pet.master.nearby.dagger.PetMasterNearbyContainerComponent
import personal.rowan.petfinder.util.AndroidUtils
import personal.rowan.petfinder.util.PermissionUtils
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class PetMasterNearbyContainerFragment : BaseFragment() {

    @Inject
    lateinit var mUserLocationManager: UserLocationManager

    private val toolbar: Toolbar by bindView(R.id.pet_master_nearby_container_toolbar)
    private val tabLayout: TabLayout by bindView(R.id.pet_master_nearby_container_tabs)
    private val viewPager: ViewPager by bindView(R.id.pet_master_nearby_container_pager)
    private val locationRationale: LinearLayout by bindView(R.id.pet_master_nearby_container_location_container)
    private val locationButton: Button by bindView(R.id.pet_master_nearby_container_location_button)
    private val locationError: LinearLayout by bindView(R.id.pet_master_nearby_container_location_failure_container)
    private val locationErrorButton: Button by bindView(R.id.pet_master_nearby_container_location_failure_button)
    private val zipcodeEntry: EditText by bindView(R.id.pet_master_nearby_container_zipcode_entry)
    private val zipcodeEntryButton: Button by bindView(R.id.pet_master_nearby_container_zipcode_entry_button)

    private var mLocationCompositeSubscription = CompositeSubscription()

    companion object {
        fun getInstance(): PetMasterNearbyContainerFragment {
            return PetMasterNearbyContainerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_master_nearby_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PetMasterNearbyContainerComponent.injector.call(this)
        setToolbar(toolbar, getString(R.string.pet_master_nearby_container_title))
        locationButton.setOnClickListener { handleLocationPermission() }
        locationErrorButton.setOnClickListener { findZipcode() }
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
            setupViewPagerWithZipcode(zipcodeEntry.text.toString())
            AndroidUtils.hideKeyboard(context!!, zipcodeEntry)
        }

        mLocationCompositeSubscription.add(mUserLocationManager.permissionObservable()
                .subscribe ({ enabled -> if(enabled) findZipcode() else showLocationRationale() },
                        { _ -> showLocationFailure() }))

        if(!PermissionUtils.hasPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
            handleLocationPermission()
            return
        }

        findZipcode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(!mLocationCompositeSubscription.isUnsubscribed) {
            mLocationCompositeSubscription.unsubscribe()
        }
    }

    override fun onStart() {
        super.onStart()
        if(PermissionUtils.hasPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) && viewPager.adapter == null) {
            findZipcode()
        }
    }

    private fun handleLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionUtils.PERMISSION_CODE_LOCATION)
    }

    private fun findZipcode() {
        if (!progressDialogShowing()) showProgressDialog(getString(R.string.pet_master_container_location_progress_title), getString(R.string.pet_master_container_location_progress_detail))
        mLocationCompositeSubscription.add(mUserLocationManager.zipcodeObservable(context!!)
                .subscribe( { zipcode -> setupViewPagerWithZipcode(zipcode) },
                        { _ -> showLocationFailure() }))
    }

    private fun setupViewPagerWithZipcode(zipcode: String) {
        dismissProgressDialog()
        if (zipcode == UserLocationManager.ERROR) {
            showLocationFailure()
            return
        }
        if (viewPager.adapter == null) {
            viewPager.setAdapter(PetMasterNearbyContainerAdapter(childFragmentManager, context!!, zipcode))
        }
        viewPager.offscreenPageLimit = PetMasterNearbyContainerAdapter.NUM_PAGES
        showTabs()
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun showLocationRationale() {
        locationRationale.visibility = View.VISIBLE
        locationError.visibility = View.GONE
        tabLayout.visibility = View.GONE
    }

    private fun showLocationFailure() {
        locationRationale.visibility = View.GONE
        locationError.visibility = View.VISIBLE
        tabLayout.visibility = View.GONE
    }

    private fun showTabs() {
        locationRationale.visibility = View.GONE
        locationError.visibility = View.GONE
        tabLayout.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_LOCATION ->
                    mUserLocationManager.permissionEvent(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

}