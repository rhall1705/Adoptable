package personal.rowan.petfinder.ui.shelter

import android.Manifest
import android.content.pm.PackageManager
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
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterContainerActivity
import personal.rowan.petfinder.ui.shelter.dagger.ShelterComponent
import personal.rowan.petfinder.ui.shelter.recycler.ShelterAdapter
import personal.rowan.petfinder.util.IntentUtils
import personal.rowan.petfinder.util.PermissionUtils
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class ShelterFragment : BasePresenterFragment<ShelterPresenter, ShelterView>(), ShelterView {

    @Inject
    lateinit var mPresenterFactory: ShelterPresenterFactory
    @Inject
    lateinit var mUserLocationManager: UserLocationManager

    companion object {

        fun getInstance(): ShelterFragment {
            return ShelterFragment()
        }
    }

    private val toolbar: Toolbar by bindView(R.id.shelter_toolbar)
    private val swipeRefresh: SwipeRefreshLayout by bindView(R.id.shelter_swipe_refresh)
    private val shelterList: RecyclerView by bindView(R.id.shelter_recycler)
    private val pagination: ProgressBar by bindView(R.id.shelter_pagination)
    private val emptyView: TextView by bindView(R.id.shelter_empty_message)
    private val locationRationale: LinearLayout by bindView(R.id.shelter_container_location_container)
    private val locationButton: Button by bindView(R.id.shelter_container_location_button)

    private lateinit var mPresenter: ShelterPresenter
    private val mAdapter = ShelterAdapter(ArrayList<ShelterViewModel>())
    private val mLayoutManager = LinearLayoutManager(context)
    private val mCompositeSubscription = CompositeSubscription()

    override fun beforePresenterPrepared() {
        ShelterComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_shelter, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar, getString(R.string.shelter_title))
        shelterList.layoutManager = mLayoutManager
        shelterList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData(context) }
        locationButton.setOnClickListener { handleLocationPermission() }
    }

    override fun onPresenterPrepared(presenter: ShelterPresenter) {
        mPresenter = presenter

        mCompositeSubscription.add(mUserLocationManager.permissionObservable().subscribe { enabled -> if(enabled) findZipcode() })

        if(!PermissionUtils.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            handleLocationPermission()
            return
        }

        findZipcode()
    }

    private fun findZipcode() {
        mCompositeSubscription.add(mUserLocationManager.zipcodeObservable().subscribe { zipcode -> setupRecyclerWithZipcode(zipcode) })
        mUserLocationManager.getZipcode(context)
    }

    private fun setupRecyclerWithZipcode(zipcode: String) {
        swipeRefresh.visibility = View.VISIBLE
        locationRationale.visibility = View.GONE

        mPresenter.loadData(context, zipcode)
        mPresenter.bindRecyclerView(context, RxRecyclerView.scrollEvents(shelterList))
        mCompositeSubscription.add(mAdapter.petsButtonObservable().subscribe { pair -> mPresenter.onPetsClicked(pair) })
        mCompositeSubscription.add(mAdapter.directionsButtonObservable().subscribe { address -> mPresenter.onDirectionsClicked(address) })
    }

    private fun handleLocationPermission() {
        swipeRefresh.visibility = View.GONE
        locationRationale.visibility = View.VISIBLE
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionUtils.PERMISSION_CODE_LOCATION)
    }

    override fun onStart() {
        super.onStart()
        if(PermissionUtils.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) && shelterList.adapter == null) {
            findZipcode()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_LOCATION ->
                mUserLocationManager.permissionEvent(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onPresenterDestroyed() {
        if(!mCompositeSubscription.isUnsubscribed) {
            mCompositeSubscription.unsubscribe()
        }
    }

    override val presenterFactory: PresenterFactory<ShelterPresenter>
        get() = mPresenterFactory

    override fun displayShelters(shelters: List<ShelterViewModel>) {
        if(shelters.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            mAdapter.paginateData(shelters)
        }
    }

    override fun onPetsButtonClicked(pair: Pair<String?, String?>) {
        startActivity(PetMasterShelterContainerActivity.getIntent(context, pair.first, pair.second))
    }

    override fun onDirectionsButtonClicked(address: String) {
        startActivity(IntentUtils.addressIntent(address))
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