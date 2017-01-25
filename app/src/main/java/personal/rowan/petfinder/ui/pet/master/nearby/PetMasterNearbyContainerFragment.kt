package personal.rowan.petfinder.ui.pet.master.nearby

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import butterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.util.PermissionUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetMasterNearbyContainerFragment : BaseFragment() {

    private val toolbar: Toolbar by bindView(R.id.pet_master_nearby_container_toolbar)
    private val tabLayout: TabLayout by bindView(R.id.pet_master_nearby_container_tabs)
    private val viewPager: ViewPager by bindView(R.id.pet_master_nearby_container_pager)
    private val locationRationale: LinearLayout by bindView(R.id.pet_master_nearby_container_location_container)
    private val locationButton: Button by bindView(R.id.pet_master_nearby_container_location_button)

    companion object {
        fun getInstance(): PetMasterNearbyContainerFragment {
            return PetMasterNearbyContainerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_master_nearby_container, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar, getString(R.string.pet_master_nearby_container_title))
        locationButton.setOnClickListener { handleLocationPermission() }

        if(!PermissionUtils.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            handleLocationPermission()
            return
        }

        setupViewPagerWithLocation()
    }

    override fun onStart() {
        super.onStart()
        if(PermissionUtils.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) && viewPager.adapter == null) {
            setupViewPagerWithLocation()
        }
    }

    private fun handleLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionUtils.PERMISSION_CODE_LOCATION)
    }

    private fun setupViewPagerWithLocation() {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), false))
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        viewPager.setAdapter(PetMasterNearbyContainerAdapter(childFragmentManager, context, addresses.get(0).postalCode))
        viewPager.offscreenPageLimit = PetMasterNearbyContainerAdapter.NUM_PAGES
        locationRationale.visibility = View.GONE
        tabLayout.visibility = View.VISIBLE
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_LOCATION ->
                    if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        setupViewPagerWithLocation()
                    }
        }
    }

}