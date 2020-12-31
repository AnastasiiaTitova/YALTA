package com.yalta.di.modules

import com.yalta.activities.fragments.AdminPointsFragment
import com.yalta.activities.fragments.DriverBrowseFragment
import com.yalta.activities.fragments.ProfileFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun provideProfileFragment(): ProfileFragment = ProfileFragment()

    @Provides
    fun provideDriverBrowseFragment(): DriverBrowseFragment = DriverBrowseFragment()

    @Provides
    fun provideAdminPointFragment(): AdminPointsFragment = AdminPointsFragment()
}
