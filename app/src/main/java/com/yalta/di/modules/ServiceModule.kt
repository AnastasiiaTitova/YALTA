package com.yalta.di.modules

import com.yalta.services.Storage
import com.yalta.services.StorageImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ServiceModule {
    @Binds
    abstract fun provideStorage(storage: StorageImpl): Storage
}
