package com.yalta.di.modules

import com.yalta.repositories.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun bindLoginRepo(repo: RealLoginRepo): LoginRepo

    @Binds
    abstract fun bindUserRepo(repo: RealUserRepo): UserRepo

    @Binds
    abstract fun bindLogoutRepo(repo: RealLogoutRepo): LogoutRepo

    @Binds
    abstract fun bindPointRepo(repo: RealPointRepo): PointRepo

    @Binds
    abstract fun bindPassRepo(repo: RealPasswordRepo): PasswordRepo

    @Binds
    abstract fun bindLocationRepo(repo: RealLocationRepo): LocationRepo
}