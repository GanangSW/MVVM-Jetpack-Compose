package com.gsw.mvvmjetpackcompose.data.api

import com.gsw.mvvmjetpackcompose.models.ApiResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET("?inc=name")
    suspend fun getUserName(): ApiResponse

    @GET("?inc=location")
    suspend fun getUserLocation(): ApiResponse

    @GET("?inc=picture")
    suspend fun getUserPicture(): ApiResponse
}