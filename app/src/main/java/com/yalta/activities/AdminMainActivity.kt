package com.yalta.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalta.R
import com.yalta.activities.fragments.AdminPointsFragment
import com.yalta.activities.fragments.ProfileFragment
import com.yalta.di.YaltaApplication
import javax.inject.Inject

class AdminMainActivity : AppCompatActivity() {

    @Inject
    lateinit var pointsFragment: AdminPointsFragment
    @Inject
    lateinit var profileFragment: ProfileFragment
    lateinit var currentFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        YaltaApplication.appComponent.inject(this)

        val navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        currentFragment = pointsFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.container, pointsFragment)
            .add(R.id.container, profileFragment)
            .hide(profileFragment)
            .commit()

        navigation.setOnNavigationItemSelectedListener {
            val fragment: Fragment =
                when (it.itemId) {
                    R.id.points -> pointsFragment
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
        navigation.selectedItemId = R.id.points
    }

    override fun onBackPressed() {}
}
