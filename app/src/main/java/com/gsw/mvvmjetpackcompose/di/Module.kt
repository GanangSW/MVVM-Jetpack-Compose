package com.gsw.mvvmjetpackcompose.di

import android.content.Context
import androidx.room.Room
import com.gsw.mvvmjetpackcompose.data.api.ApiInterface
import com.gsw.mvvmjetpackcompose.data.local.LocalDb
import com.gsw.mvvmjetpackcompose.models.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://randomuser.me/api/"

    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun apiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun localDb(@ApplicationContext context: Context): LocalDb {
        return Room.databaseBuilder(context, LocalDb::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun userDao(db: LocalDb): UserDao = db.userDao()
}