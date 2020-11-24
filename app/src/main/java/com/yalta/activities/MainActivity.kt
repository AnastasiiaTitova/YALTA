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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navigation.setOnNavigationItemSelectedListener {
            val fragment: Fragment =
                when (it.itemId) {
                    R.id.browse -> BrowseFragment()
                    R.id.map -> MapFragment()
                    R.id.profile -> ProfileFragment()
                    else -> TODO()      // idk, we can't be here
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            true
        }
        navigation.selectedItemId = R.id.browse
    }
}
