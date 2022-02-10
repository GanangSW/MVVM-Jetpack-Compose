package com.gsw.mvvmjetpackcompose.repo

import androidx.lifecycle.LiveData
import com.gsw.mvvmjetpackcompose.data.api.ApiInterface
import com.gsw.mvvmjetpackcompose.models.User
import com.gsw.mvvmjetpackcompose.models.UserDao
import kotlinx.coroutines.delay
import javax.inject.Inject

interface UserRepo {
    suspend fun getNewUser(): User
    suspend fun deleteUser(toDelete: User)
    fun getAllUser(): LiveData<List<User>>
}

class UserRepositoryImp @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userDao: UserDao
) : UserRepo {
    override suspend fun getNewUser(): User {
        val name = apiInterface.getUserName().results[0].name
        val location = apiInterface.getUserLocation().results[0].location
        val picture = apiInterface.getUserPicture().results[0].picture
        val user = User(
            name = name?.first ?: "",
            lastName = name?.last ?: "",
            city = location?.city ?: "",
            thumbnail = picture?.thumbnail ?: ""
        )
        userDao.insert(user = user)
        return user
    }

    override suspend fun deleteUser(toDelete: User) = userDao.delete(user = toDelete)

    override fun getAllUser(): LiveData<List<User>> = userDao.getAll()

}