package personal.rowan.petfinder.ui.pet.master.shelter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import butterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity

/**
 * Created by Rowan Hall
 */
class PetMasterShelterContainerActivity : BaseActivity() {

    private val toolbar: Toolbar by bindView(R.id.pet_master_shelter_container_toolbar)
    private val tabLayout: TabLayout by bindView(R.id.pet_master_shelter_container_tabs)
    private val viewPager: ViewPager by bindView(R.id.pet_master_shelter_container_pager)

    companion object {

        private val ARG_SHELTER_ID = "PetMasterShelterContainerActivity.ShelterId"
        private val ARG_SHELTER_NAME = "PetMasterShelterContainerActivity.ShelterName"

        fun getIntent(context: Context, shelterId: String?, shelterName: String?): Intent {
            val intent: Intent = Intent(context, PetMasterShelterContainerActivity::class.java)
            intent.putExtra(ARG_SHELTER_ID, shelterId)
            intent.putExtra(ARG_SHELTER_NAME, shelterName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_master_shelter_container)
        setToolbar(toolbar, intent.getStringExtra(ARG_SHELTER_NAME), true)
        viewPager.setAdapter(PetMasterShelterContainerAdapter(supportFragmentManager, this, intent.getStringExtra(ARG_SHELTER_ID)))
        viewPager.offscreenPageLimit = PetMasterShelterContainerAdapter.NUM_PAGES
        tabLayout.setupWithViewPager(viewPager)
    }

}