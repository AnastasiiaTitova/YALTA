package com.yalta.di

import com.yalta.activities.*
import com.yalta.activities.fragments.AdminPointsFragment
import com.yalta.activities.fragments.MapFragment
import com.yalta.activities.fragments.ProfileFragment
import com.yalta.di.modules.*
import com.yalta.repositories.PointRepo
import com.yalta.repositories.RealPointRepo
import com.yalta.services.*
import com.yalta.viewmodel.ChangePasswordViewModel
import com.yalta.viewmodel.LoginViewModel
import com.yalta.viewmodel.ProfileViewModel
import dagger.Component

// Definition of a Dagger component
@Component(
    modules = [
        RepoModule::class,
        ServiceModule::class,
        ViewModelModule::class,
        DispatcherModule::class,
        FragmentModule::class]
)
interface AppComponent {

    // Classes that can be injected by this Component
    fun inject(activity: LoginActivity)

    fun inject(activity: DriverMainActivity)
    fun inject(frgament: ProfileFragment)
    fun inject(fragment: MapFragment)

    fun inject(activity: AdminMainActivity)
    fun inject(fragment: AdminPointsFragment)

    fun inject(activity: ChangePasswordActivity)
    fun inject(activity: AddPointActivity)
}
