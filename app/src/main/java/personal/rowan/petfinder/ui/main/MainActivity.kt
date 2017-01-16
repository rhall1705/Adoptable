package personal.rowan.petfinder.ui.main

import android.os.Bundle

import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.petmaster.container.PetMasterContainerFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, PetMasterContainerFragment.getInstance())
                    .commit()
        }
    }
}
