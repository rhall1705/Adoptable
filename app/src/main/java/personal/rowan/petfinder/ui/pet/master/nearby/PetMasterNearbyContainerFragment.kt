package personal.rowan.petfinder.ui.pet.master.nearby

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.location.LocationActivity
import personal.rowan.petfinder.ui.pet.master.nearby.dagger.PetMasterNearbyContainerComponent
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
        setupViewPagerWithZipcode(mUserLocationManager.loadZipcode(context!!))
    }

    private fun setupViewPagerWithZipcode(zipcode: String) {
        if (TextUtils.isEmpty(zipcode) || zipcode == UserLocationManager.ERROR) {
            startActivity(LocationActivity.createIntent(context!!).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            return
        }
        if (viewPager.adapter == null) {
            viewPager.setAdapter(PetMasterNearbyContainerAdapter(childFragmentManager, context!!, zipcode))
        }
        viewPager.offscreenPageLimit = PetMasterNearbyContainerAdapter.NUM_PAGES
        tabLayout.setupWithViewPager(viewPager)
    }

}