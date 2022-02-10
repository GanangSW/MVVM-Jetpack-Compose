package com.gsw.mvvmjetpackcompose.di

import com.gsw.mvvmjetpackcompose.models.User
import com.gsw.mvvmjetpackcompose.repo.UserRepo
import com.gsw.mvvmjetpackcompose.repo.UserRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun userRepo(repo: UserRepositoryImp): UserRepo

}