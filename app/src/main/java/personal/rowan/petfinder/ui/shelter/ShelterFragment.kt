package personal.rowan.petfinder.ui.shelter

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotterknife.bindView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.location.LocationActivity
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterContainerActivity
import personal.rowan.petfinder.ui.shelter.dagger.ShelterComponent
import personal.rowan.petfinder.ui.shelter.recycler.ShelterAdapter
import personal.rowan.petfinder.util.IntentUtils
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
    private val emptyView: TextView by bindView(R.id.shelter_empty_message)

    private lateinit var mPresenter: ShelterPresenter
    private val mAdapter = ShelterAdapter(ArrayList())
    private val mLayoutManager = LinearLayoutManager(context)
    private val mCompositeSubscription = CompositeSubscription()

    override fun beforePresenterPrepared() {
        ShelterComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shelter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar, getString(R.string.shelter_title))
        shelterList.layoutManager = mLayoutManager
        shelterList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData(context!!) }
    }

    override fun onPresenterPrepared(presenter: ShelterPresenter) {
        mPresenter = presenter
        setupRecyclerWithZipcode(mUserLocationManager.loadZipcode(context!!))
    }

    private fun setupRecyclerWithZipcode(zipcode: String) {
        dismissProgressDialog()
        if (TextUtils.isEmpty(zipcode) || zipcode == UserLocationManager.ERROR) {
            startActivity(LocationActivity.createIntent(context!!).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            return
        }

        val context = context
        if (context != null) {
            mPresenter.initialLoad(context, zipcode)
            mPresenter.bindRecyclerView(context, RxRecyclerView.scrollEvents(shelterList))
        }
        mCompositeSubscription.add(mAdapter.petsButtonObservable().subscribe { pair -> mPresenter.onPetsClicked(pair) })
        mCompositeSubscription.add(mAdapter.directionsButtonObservable().subscribe { address -> mPresenter.onDirectionsClicked(address) })
    }

    override fun onPresenterDestroyed() {
        if(!mCompositeSubscription.isUnsubscribed) {
            mCompositeSubscription.unsubscribe()
        }
    }

    override fun presenterFactory(): PresenterFactory<ShelterPresenter> {
        return mPresenterFactory
    }

    override fun displayShelters(shelters: List<ShelterListViewState>) {
        if(shelters.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            mAdapter.paginateData(shelters)
        }
    }

    override fun onPetsButtonClicked(pair: Pair<String?, String?>) {
        startActivity(PetMasterShelterContainerActivity.getIntent(context!!, pair.first, pair.second))
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

}