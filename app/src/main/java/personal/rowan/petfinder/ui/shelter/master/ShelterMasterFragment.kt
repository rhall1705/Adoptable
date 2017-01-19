package personal.rowan.petfinder.ui.shelter.master

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.bindView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.shelter.master.dagger.ShelterMasterComponent
import personal.rowan.petfinder.ui.shelter.master.recycler.ShelterMasterAdapter
import rx.Subscription
import java.util.*
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class ShelterMasterFragment : BasePresenterFragment<ShelterMasterPresenter, ShelterMasterView>(), ShelterMasterView {

    @Inject
    lateinit var mPresenterFactory: ShelterMasterPresenterFactory

    private val toolbar: Toolbar by bindView(R.id.shelter_master_toolbar)
    private val swipeRefresh: SwipeRefreshLayout by bindView(R.id.shelter_master_swipe_refresh)
    private val petList: RecyclerView by bindView(R.id.shelter_master_recycler)
    private val pagination: ProgressBar by bindView(R.id.shelter_master_pagination)

    private lateinit var mPresenter: ShelterMasterPresenter
    private lateinit var mLocation: String
    private val mAdapter: ShelterMasterAdapter = ShelterMasterAdapter(ArrayList<Shelter>())
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private var mPetButtonSubscription: Subscription? = null
    private var mDirectionsButtonSubscription: Subscription? = null

    companion object {

        private val ARG_PET_MASTER_LOCATION = "PetMasterFragment.Arg.Location"

        fun getInstance(location: String): ShelterMasterFragment {
            val fragment: ShelterMasterFragment = ShelterMasterFragment()
            val args: Bundle = Bundle()
            args.putString(ARG_PET_MASTER_LOCATION, location)
            fragment.arguments = args
            return fragment
        }
    }

    override fun beforePresenterPrepared() {
        ShelterMasterComponent.injector.call(this)
        val args: Bundle = arguments
        mLocation = args.getString(ARG_PET_MASTER_LOCATION)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_shelter_master, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar, getString(R.string.shelter_master_title))
        petList.layoutManager = mLayoutManager
        petList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData() }
    }

    override fun onPresenterPrepared(presenter: ShelterMasterPresenter) {
        mPresenter = presenter
        mPresenter.loadData(mLocation)
        mPresenter.bindRecyclerView(RxRecyclerView.scrollEvents(petList))
        mPetButtonSubscription = mAdapter.petsButtonObservable().subscribe { shelter -> mPresenter.onPetsClicked(shelter) }
        mDirectionsButtonSubscription = mAdapter.directionsButtonObservable().subscribe { shelter -> mPresenter.onDirectionsClicked(shelter) }
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
        // todo: implement navigation
        showToastMessage("Navigate to pets for " + shelter.name!!.`$t`)
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