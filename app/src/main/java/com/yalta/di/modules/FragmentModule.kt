package com.yalta.di.modules

import com.yalta.activities.fragments.AdminPointsFragment
import com.yalta.activities.fragments.DriverRoutesFragment
import com.yalta.activities.fragments.ProfileFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun provideProfileFragment(): ProfileFragment = ProfileFragment()

    @Provides
    fun provideDriverRoutesFragment(): DriverRoutesFragment = DriverRoutesFragment()

    @Provides
    fun provideAdminPointFragment(): AdminPointsFragment = AdminPointsFragment()
}
