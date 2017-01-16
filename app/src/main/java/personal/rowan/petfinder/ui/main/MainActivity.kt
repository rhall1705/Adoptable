package personal.rowan.petfinder.ui.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import butterknife.bindView

import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.petmaster.container.PetMasterContainerFragment

class MainActivity : BaseActivity() {

    val bottomNavigation: BottomNavigationView by bindView(R.id.main_bottom_navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragmentInContainer(PetMasterContainerFragment.getInstance())
        }

        bottomNavigation.setOnNavigationItemSelectedListener({
            menuItem ->
            when(menuItem.itemId) {
                R.id.action_nearby_animals -> showToastMessage("Navigate to pets page")
                R.id.action_nearby_shelters -> showToastMessage("Navigate to shelters page")
                R.id.action_search -> showToastMessage("Navigate to search page")
            }
            true
        })
    }

    private fun replaceFragmentInContainer(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
    }

}
