package com.yalta.di.modules

import com.yalta.repositories.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun bindLoginRepo(repo: RealAuthRepo): AuthRepo

    @Binds
    abstract fun bindPointRepo(repo: RealPointRepo): PointRepo

    @Binds
    abstract fun bindPassRepo(repo: RealPasswordRepo): PasswordRepo

    @Binds
    abstract fun bindLocationRepo(repo: RealLocationRepo): LocationRepo

    @Binds
    abstract fun bindRoutesRepo(repo: RealRoutesRepo): RoutesRepo
}
