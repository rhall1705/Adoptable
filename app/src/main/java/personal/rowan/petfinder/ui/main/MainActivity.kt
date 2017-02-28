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
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment
import personal.rowan.petfinder.ui.pet.master.nearby.PetMasterNearbyContainerFragment
import personal.rowan.petfinder.ui.search.SearchFragment
import personal.rowan.petfinder.ui.shelter.ShelterFragment

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
                R.id.action_favorites -> pager.setCurrentItem(MainPagerAdapter.POSITION_FAVORITES, false)
            }
            true
        })
    }

    private class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        companion object {
            val NUM_PAGES = 4

            val POSITION_NEARBY_ANIMALS = 0
            val POSITION_NEARBY_SHELTERS = 1
            val POSITION_SEARCH = 2
            val POSITION_FAVORITES = 3
        }

        override fun getItem(position: Int): Fragment {
            when(position) {
                POSITION_NEARBY_ANIMALS -> return PetMasterNearbyContainerFragment.getInstance()
                POSITION_NEARBY_SHELTERS -> return ShelterFragment.getInstance()
                POSITION_SEARCH -> return SearchFragment.getInstance()
                POSITION_FAVORITES -> return PetMasterFragment.getInstance()
                else -> throw RuntimeException("Invalid viewpager position")
            }
        }

        override fun getCount(): Int {
            return NUM_PAGES
        }

    }

}
