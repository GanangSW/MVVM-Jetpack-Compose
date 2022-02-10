package com.gsw.mvvmjetpackcompose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gsw.mvvmjetpackcompose.data.api.ApiInterface
import com.gsw.mvvmjetpackcompose.models.User
import com.gsw.mvvmjetpackcompose.models.UserDao
import com.gsw.mvvmjetpackcompose.repo.UserRepo
import com.gsw.mvvmjetpackcompose.repo.UserRepositoryImp
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.nio.charset.StandardCharsets


private val user1 =
    User(name = "Ganang", lastName = "Samudra", city = "Surakarta", thumbnail = "http://..")
private val user2 =
    User(name = "Samudra", lastName = "Wibawa", city = "Surakarta", thumbnail = "http://..")

class UserRepoTest {
    private val mockWebServer = MockWebServer().apply {
        url("/")
        dispatcher = myDispatcher
    }

    private val apiInterface = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)

    private val newsRepository = UserRepositoryImp(apiInterface, MockUserDao())

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Users on the DB are retrived correctly`() {
        val users = newsRepository.getAllUser()
        assertEquals(2, users.value?.size)
    }

    @Test
    fun `Users is deleted correctly`() {
        runBlocking {
            newsRepository.deleteUser(user1)

            val users = newsRepository.getAllUser()
            assertEquals(1, users.value?.size)
        }
    }

    @Test
    fun `Users is fetched correctly`() {
        runBlocking {
            val newUser = newsRepository.getNewUser()
            val users = newsRepository.getAllUser()
            assertEquals(3, users.value?.size)
            assertEquals(newUser.name, "Wayne")
            assertEquals(newUser.lastName, "Collins")
            assertEquals(newUser.city, "Cairns")
            assert(newUser.thumbnail.contains("thumb/women/78.jpg"))
        }
    }
}

class MockUserDao : UserDao {

    private val users = MutableLiveData(listOf(user1, user2))

    override fun insert(user: User) {
        users.value = users.value?.toMutableList()?.apply { add(user) }
    }

    override fun getAll(): LiveData<List<User>> = users

    override fun delete(user: User) {
        users.value = users.value?.toMutableList()?.apply { remove(user) }
    }

}

val myDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/?inc=name" -> MockResponse().apply { addResponse("api_name.json") }
            "/?inc=location" -> MockResponse().apply { addResponse("api_location.json") }
            "/?inc=picture" -> MockResponse().apply { addResponse("api_picture.json") }
            else -> MockResponse().setResponseCode(404)
        }
    }

}

fun MockResponse.addResponse(filePath: String): MockResponse {
    val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
    val source = inputStream?.source()?.buffer()
    source?.let {
        setResponseCode(200)
        setBody(it.readString(StandardCharsets.UTF_8))
    }
    return this
}