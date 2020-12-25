package com.yalta.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalta.R
import com.yalta.activities.fragments.BlankFragment
import com.yalta.activities.fragments.DriverBrowseFragment
import com.yalta.activities.fragments.MapFragment
import com.yalta.activities.fragments.ProfileFragment
import com.yalta.utils.ViewUtils.grantedLocationPermission

class DriverMainActivity : AppCompatActivity() {
    private lateinit var browseFragment: Fragment
    private lateinit var mapFragment: Fragment
    private lateinit var profileFragment: Fragment
    private lateinit var currentFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_main)

        val navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        browseFragment = DriverBrowseFragment()
        profileFragment = ProfileFragment()
        currentFragment = browseFragment

        mapFragment = if (grantedLocationPermission()) {
            MapFragment()
        } else {
            BlankFragment()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.container, browseFragment)
            .add(R.id.container, mapFragment)
            .hide(mapFragment)
            .add(R.id.container, profileFragment)
            .hide(profileFragment)
            .commit()

        navigation.setOnNavigationItemSelectedListener {
            val fragment: Fragment =
                when (it.itemId) {
                    R.id.browse -> browseFragment
                    R.id.map -> mapFragment
                    R.id.profile -> profileFragment
                    else -> TODO()      // idk, we can't be here
                }
            supportFragmentManager.beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit()
            currentFragment = fragment
            true
        }
        navigation.selectedItemId = R.id.browse
    }

    override fun onBackPressed() { }
}
