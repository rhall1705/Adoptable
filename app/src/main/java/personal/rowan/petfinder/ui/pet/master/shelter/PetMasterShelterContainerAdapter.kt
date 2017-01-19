package personal.rowan.petfinder.ui.pet.master.shelter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
class PetMasterShelterContainerAdapter(fm: FragmentManager, val mContext: Context, val mShelterId: String) : FragmentStatePagerAdapter(fm) {

    companion object {
        val NUM_PAGES = 4

        private val POSITION_ADOPTABLE = 0
        private val POSITION_HOLD = 1
        private val POSITION_PENDING = 2
        private val POSITION_ADOPTED = 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            POSITION_ADOPTABLE -> return PetMasterFragment.getInstance(mShelterId, PetMasterFragment.STATUS_OPTION_ADOPTABLE)
            POSITION_HOLD -> return PetMasterFragment.getInstance(mShelterId, PetMasterFragment.STATUS_OPTION_HOLD)
            POSITION_PENDING -> return PetMasterFragment.getInstance(mShelterId, PetMasterFragment.STATUS_OPTION_PENDING)
            POSITION_ADOPTED -> return PetMasterFragment.getInstance(mShelterId, PetMasterFragment.STATUS_OPTION_ADOPTED)
            else -> throw RuntimeException("Invalid viewpager position")
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            POSITION_ADOPTABLE -> return mContext.getString(R.string.pet_master_tab_adoptable)
            POSITION_HOLD -> return mContext.getString(R.string.pet_master_tab_hold)
            POSITION_PENDING -> return mContext.getString(R.string.pet_master_tab_pending)
            POSITION_ADOPTED -> return mContext.getString(R.string.pet_master_tab_adopted)
            else -> throw RuntimeException("Invalid viewpager position")
        }
    }

}