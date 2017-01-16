package personal.rowan.petfinder.ui.petmaster.container

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseFragment

/**
 * Created by Rowan Hall
 */
class PetMasterContainerFragment : BaseFragment() {

    val toolbar: Toolbar by bindView(R.id.pet_master_container_toolbar)
    val tabLayout: TabLayout by bindView(R.id.pet_master_container_tabs)
    val viewPager: ViewPager by bindView(R.id.pet_master_container_pager)

    companion object {
        fun getInstance(): PetMasterContainerFragment {
            return PetMasterContainerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pet_master_container, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar, getString(R.string.pet_master_container_title))
        viewPager.setAdapter(PetMasterContainerAdapter(fragmentManager, context, "30308"))
        tabLayout.setupWithViewPager(viewPager)
    }

}