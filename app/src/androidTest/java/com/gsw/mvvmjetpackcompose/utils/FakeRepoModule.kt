package com.gsw.mvvmjetpackcompose.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gsw.mvvmjetpackcompose.di.RepoModule
import com.gsw.mvvmjetpackcompose.models.User
import com.gsw.mvvmjetpackcompose.repo.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class]
)
class FakeRepoModule {

    private val users = MutableLiveData<List<User>>(listOf())

    @Provides
    @Singleton
    fun userRepository(): UserRepo = object : UserRepo {
        override suspend fun getNewUser(): User {
            val userList = users.value!!
            val newUser = User(
                "Name ${userList.size}",
                "LastName ${userList.size}",
                "City",
                "Image"
            )
            users.postValue(users.value?.toMutableList()?.apply { add(newUser) })
            return newUser

        }

        override suspend fun deleteUser(toDelete: User) {
            users.postValue(users.value?.toMutableList()?.apply { remove(toDelete) })
        }

        override fun getAllUser(): LiveData<List<User>> {
            return users
        }

    }
}