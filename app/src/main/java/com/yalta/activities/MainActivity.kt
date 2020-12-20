package com.yalta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalta.R
import com.yalta.activities.fragments.BrowseFragment
import com.yalta.activities.fragments.MapFragment
import com.yalta.activities.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    private val browseFragment = BrowseFragment()
    private val mapFragment = MapFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, browseFragment)
            .add(R.id.container, mapFragment)
            .hide(mapFragment)
            .add(R.id.container, profileFragment)
            .hide(profileFragment)
            .commit()

        var currentFragment: Fragment = browseFragment

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

    override fun onPause() {
        supportFragmentManager.beginTransaction()
            .detach(browseFragment)
            .detach(mapFragment)
            .detach(profileFragment)
            .commit()
        super.onPause()
    }
}
