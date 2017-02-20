package personal.rowan.petfinder.ui.pet.master

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.detail.PetDetailActivity
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterComponent
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterAdapter
import rx.Subscription
import java.util.*
import javax.inject.Inject
import android.support.v4.app.ActivityOptionsCompat
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchArguments


/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterFragment : BasePresenterFragment<PetMasterPresenter, PetMasterView>(), PetMasterView {

    @Inject
    lateinit var mPresenterFactory: PetMasterPresenterFactory

    private val swipeRefresh: SwipeRefreshLayout by bindView(R.id.pet_master_swipe_refresh)
    private val petList: RecyclerView by bindView(R.id.pet_master_recycler)
    private val pagination: ProgressBar by bindView(R.id.pet_master_pagination)
    private val emptyView: TextView by bindView(R.id.pet_master_empty_message)

    private var mType: Int? = null

    private var mSearchParams: PetMasterSearchArguments? = null

    private var mShelterId: String? = null
    private var mStatus: Char? = null

    private lateinit var mPresenter: PetMasterPresenter
    private val mAdapter: PetMasterAdapter = PetMasterAdapter(ArrayList<Pet>())
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private var mItemClickSubscription: Subscription? = null

    companion object {

        private val ARG_PET_MASTER_TYPE = "PetMasterFragment.Arg.Type"
        val TYPE_FIND = 0
        val TYPE_SHELTER = 1

        private val ARG_PET_MASTER_SEARCH_PARAMS = "PetMasterFragment.Arg.SearchParams"

        private val ARG_PET_MASTER_SHELTER_ID = "PetMasterFragment.Arg.ShelterId"
        private val ARG_PET_MASTER_STATUS = "PetMasterFragment.Arg.Status"

        val STATUS_OPTION_ADOPTABLE = 'A'
        val STATUS_OPTION_HOLD = 'H'
        val STATUS_OPTION_PENDING = 'P'
        val STATUS_OPTION_ADOPTED = 'X'

        @JvmOverloads fun getInstance(location: String,
                                      animal: String? = "",
                                      size: String? = "",
                                      age: String? = "",
                                      sex: String? = "",
                                      breed: String? = ""): PetMasterFragment {
            val fragment: PetMasterFragment = PetMasterFragment()
            val args: Bundle = Bundle()
            args.putInt(ARG_PET_MASTER_TYPE, TYPE_FIND)
            args.putParcelable(ARG_PET_MASTER_SEARCH_PARAMS, PetMasterSearchArguments(location, animal, size, age, sex, breed))
            fragment.arguments = args
            return fragment
        }

        fun getInstance(shelterId: String, status: Char): PetMasterFragment {
            val fragment: PetMasterFragment = PetMasterFragment()
            val args: Bundle = Bundle()
            args.putInt(ARG_PET_MASTER_TYPE, TYPE_SHELTER)
            args.putString(ARG_PET_MASTER_SHELTER_ID, shelterId)
            args.putChar(ARG_PET_MASTER_STATUS, status)
            fragment.arguments = args
            return fragment
        }
    }

    override fun beforePresenterPrepared() {
        PetMasterComponent.injector.call(this)
        val args: Bundle = arguments

        mType = args.getInt(ARG_PET_MASTER_TYPE)
        when(mType) {
            TYPE_FIND -> {
                mSearchParams = args.getParcelable(ARG_PET_MASTER_SEARCH_PARAMS)
            }
            TYPE_SHELTER -> {
                mShelterId = args.getString(ARG_PET_MASTER_SHELTER_ID)
                mStatus = args.getChar(ARG_PET_MASTER_STATUS)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_master, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petList.layoutManager = mLayoutManager
        petList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData() }
    }

    override fun onPresenterPrepared(presenter: PetMasterPresenter) {
        mPresenter = presenter
        when(mType) {
            TYPE_FIND -> mPresenter.loadData(mSearchParams!!.location(), mSearchParams!!.animal(), mSearchParams!!.size(), mSearchParams!!.age(), mSearchParams!!.sex(), mSearchParams!!.breed())
            TYPE_SHELTER -> mPresenter.loadData(mShelterId!!, mStatus!!)
        }
        mPresenter.bindRecyclerView(RxRecyclerView.scrollEvents(petList))
        mItemClickSubscription = mAdapter.itemClickObservable().subscribe { clickData -> mPresenter.onPetClicked(clickData) }
    }

    override fun onPresenterDestroyed() {
        if(mItemClickSubscription!= null && !mItemClickSubscription!!.isUnsubscribed) {
            mItemClickSubscription!!.unsubscribe()
        }
    }

    override val presenterFactory: PresenterFactory<PetMasterPresenter>
        get() = mPresenterFactory

    override fun displayPets(pets: List<Pet>) {
        if(pets.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            mAdapter.paginateData(pets)
        }
    }

    override fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *petMasterClickData.transitionViews().toTypedArray())
        startActivity(PetDetailActivity.createIntent(context, petMasterClickData.pet()), options.toBundle())
    }

    override fun shouldPaginate(): Boolean {
        return mLayoutManager.findLastVisibleItemPosition() >= mAdapter.itemCount - 1
    }

    override fun showError(error: String) {
        Log.d("petmastererror", error)
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