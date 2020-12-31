package com.yalta.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalta.R
import com.yalta.activities.fragments.BlankFragment
import com.yalta.activities.fragments.DriverRoutesFragment
import com.yalta.activities.fragments.MapFragment
import com.yalta.activities.fragments.ProfileFragment
import com.yalta.di.YaltaApplication
import com.yalta.utils.ViewUtils.grantedLocationPermission
import javax.inject.Inject

class DriverMainActivity : AppCompatActivity() {

    @Inject
    lateinit var routesFragment: DriverRoutesFragment
    @Inject
    lateinit var profileFragment: ProfileFragment
    private lateinit var mapFragment: Fragment
    private lateinit var currentFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_main)

        YaltaApplication.appComponent.inject(this)

        val navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        currentFragment = routesFragment

        mapFragment = if (grantedLocationPermission()) {
            MapFragment()
        } else {
            BlankFragment()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.container, routesFragment)
            .add(R.id.container, mapFragment)
            .hide(mapFragment)
            .add(R.id.container, profileFragment)
            .hide(profileFragment)
            .commit()

        navigation.setOnNavigationItemSelectedListener {
            val fragment: Fragment =
                when (it.itemId) {
                    R.id.routes -> routesFragment
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
        navigation.selectedItemId = R.id.routes
    }

    override fun onBackPressed() {}
}
