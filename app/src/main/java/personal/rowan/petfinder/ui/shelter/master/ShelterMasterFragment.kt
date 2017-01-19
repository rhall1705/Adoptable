package personal.rowan.petfinder.ui.shelter.master

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import butterknife.bindView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterContainerActivity
import personal.rowan.petfinder.ui.shelter.master.dagger.ShelterMasterComponent
import personal.rowan.petfinder.ui.shelter.master.recycler.ShelterMasterAdapter
import personal.rowan.petfinder.util.PermissionUtils
import rx.Subscription
import java.util.*
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class ShelterMasterFragment : BasePresenterFragment<ShelterMasterPresenter, ShelterMasterView>(), ShelterMasterView {

    @Inject
    lateinit var mPresenterFactory: ShelterMasterPresenterFactory

    companion object {

        fun getInstance(): ShelterMasterFragment {
            return ShelterMasterFragment()
        }
    }

    private val toolbar: Toolbar by bindView(R.id.shelter_master_toolbar)
    private val swipeRefresh: SwipeRefreshLayout by bindView(R.id.shelter_master_swipe_refresh)
    private val shelterList: RecyclerView by bindView(R.id.shelter_master_recycler)
    private val pagination: ProgressBar by bindView(R.id.shelter_master_pagination)
    private val locationRationale: LinearLayout by bindView(R.id.shelter_master_container_location_container)
    private val locationButton: Button by bindView(R.id.shelter_master_container_location_button)

    private lateinit var mPresenter: ShelterMasterPresenter
    private val mAdapter: ShelterMasterAdapter = ShelterMasterAdapter(ArrayList<Shelter>())
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private var mPetButtonSubscription: Subscription? = null
    private var mDirectionsButtonSubscription: Subscription? = null

    override fun beforePresenterPrepared() {
        ShelterMasterComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_shelter_master, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar, getString(R.string.shelter_master_title))
        shelterList.layoutManager = mLayoutManager
        shelterList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData() }
        locationButton.setOnClickListener { handleLocationPermission() }
    }

    override fun onPresenterPrepared(presenter: ShelterMasterPresenter) {
        mPresenter = presenter

        if(!PermissionUtils.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            handleLocationPermission()
            return
        }

        setupRecycler()
    }

    private fun setupRecycler() {
        swipeRefresh.visibility = View.VISIBLE
        locationRationale.visibility = View.GONE

        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), false))
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        mPresenter.loadData(addresses.get(0).postalCode)
        mPresenter.bindRecyclerView(RxRecyclerView.scrollEvents(shelterList))
        mPetButtonSubscription = mAdapter.petsButtonObservable().subscribe { shelter -> mPresenter.onPetsClicked(shelter) }
        mDirectionsButtonSubscription = mAdapter.directionsButtonObservable().subscribe { shelter -> mPresenter.onDirectionsClicked(shelter) }
    }

    private fun handleLocationPermission() {
        swipeRefresh.visibility = View.GONE
        locationRationale.visibility = View.VISIBLE
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionUtils.PERMISSION_CODE_LOCATION)
    }

    override fun onStart() {
        super.onStart()
        if(PermissionUtils.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) && shelterList.adapter == null) {
            setupRecycler()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_LOCATION ->
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupRecycler()
                }
        }
    }

    override fun onPresenterDestroyed() {
        if(mPetButtonSubscription != null && !mPetButtonSubscription!!.isUnsubscribed) {
            mPetButtonSubscription!!.unsubscribe()
        }
        if(mDirectionsButtonSubscription != null && !mDirectionsButtonSubscription!!.isUnsubscribed) {
            mDirectionsButtonSubscription!!.unsubscribe()
        }
    }

    override val presenterFactory: PresenterFactory<ShelterMasterPresenter>
        get() = mPresenterFactory

    override fun displayShelters(shelters: List<Shelter>) {
        mAdapter.paginateData(shelters)
    }

    override fun onPetsButtonClicked(shelter: Shelter) {
        startActivity(PetMasterShelterContainerActivity.getIntent(context, shelter.id!!.`$t`!!))
    }

    override fun onDirectionsButtonClicked(shelter: Shelter) {
        // todo: implement maps intent
        showToastMessage("Go to Gmaps intent for " + shelter.name!!.`$t`)
    }

    override fun shouldPaginate(): Boolean {
        return mLayoutManager.findLastVisibleItemPosition() >= mAdapter.itemCount - 1
    }

    override fun showError(error: String) {
        Log.d("sheltermastererror", error)
        showToastMessage(error)
    }

    override fun showProgress() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefresh.isRefreshing = false
    }

    override fun showPagination() {
        pagination.visibility = View.VISIBLE
    }

    override fun hidePagination() {
        pagination.visibility = View.GONE
    }

}