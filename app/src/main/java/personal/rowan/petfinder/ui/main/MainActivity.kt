package personal.rowan.petfinder.ui.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import butterknife.bindView

import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.pet.master.container.PetMasterNearbyContainerFragment
import personal.rowan.petfinder.ui.search.SearchFragment
import personal.rowan.petfinder.ui.shelter.master.ShelterMasterFragment

class MainActivity : BaseActivity() {

    val pager: ViewPager by bindView(R.id.main_pager)
    val bottomNavigation: BottomNavigationView by bindView(R.id.main_bottom_navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.adapter = MainPagerAdapter(supportFragmentManager)
        pager.offscreenPageLimit = MainPagerAdapter.NUM_PAGES

        bottomNavigation.setOnNavigationItemSelectedListener({ menuItem ->
            when(menuItem.itemId) {
                R.id.action_nearby_animals -> pager.setCurrentItem(MainPagerAdapter.POSITION_NEARBY_ANIMALS, false)
                R.id.action_nearby_shelters -> pager.setCurrentItem(MainPagerAdapter.POSITION_NEARBY_SHELTERS, false)
                R.id.action_search -> pager.setCurrentItem(MainPagerAdapter.POSITION_SEARCH, false)
            }
            true
        })
    }

    private class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        companion object {
            val NUM_PAGES = 3

            val POSITION_NEARBY_ANIMALS = 0
            val POSITION_NEARBY_SHELTERS = 1
            val POSITION_SEARCH = 2
        }

        override fun getItem(position: Int): Fragment {
            when(position) {
                POSITION_NEARBY_ANIMALS -> return PetMasterNearbyContainerFragment.getInstance()
                POSITION_NEARBY_SHELTERS -> return ShelterMasterFragment.getInstance()
                POSITION_SEARCH -> return SearchFragment()
                else -> throw RuntimeException("Invalid viewpager position")
            }
        }

        override fun getCount(): Int {
            return NUM_PAGES
        }

    }

}
