package personal.rowan.petfinder.ui.petmaster

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.bindView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.petmaster.dagger.PetMasterComponent
import personal.rowan.petfinder.ui.petmaster.dagger.PetMasterScope
import personal.rowan.petfinder.ui.petmaster.recycler.PetMasterAdapter
import java.util.*
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterFragment : BasePresenterFragment<PetMasterPresenter, PetMasterView>(), PetMasterView {

    @Inject
    lateinit var mPresenterFactory: PetMasterPresenterFactory

    val swipeRefresh: SwipeRefreshLayout by bindView(R.id.pet_master_swipe_refresh)
    val petList: RecyclerView by bindView(R.id.pet_master_recycler)
    val pagination: ProgressBar by bindView(R.id.pet_master_pagination)

    lateinit var mPresenter: PetMasterPresenter
    val mAdapter: PetMasterAdapter = PetMasterAdapter(ArrayList<Pet>())
    val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)

    companion object {
        fun getInstance(): PetMasterFragment {
            return PetMasterFragment()
        }
    }

    override fun beforePresenterPrepared() {
        PetMasterComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_master, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petList.layoutManager = mLayoutManager
        petList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData("30308") }
    }

    override fun onPresenterPrepared(presenter: PetMasterPresenter) {
        mPresenter = presenter
        mPresenter.refreshData("30308")
        mPresenter.bindRecyclerView(RxRecyclerView.scrollEvents(petList))
    }

    override val presenterFactory: PresenterFactory<PetMasterPresenter>
        get() = mPresenterFactory

    override fun displayPets(pets: List<Pet>) {
        mAdapter.paginateData(pets)
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