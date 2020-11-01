package com.yalta.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            when (it.itemId) {
                R.id.map -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, MapFragment()).commit()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment()).commit()
                }
                R.id.browse -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, BrowseFragment()).commit()
                }
            }
            true
        }
        navigation.selectedItemId = R.id.browse
    }
}
