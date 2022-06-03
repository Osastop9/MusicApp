package com.example.musicapptest.api.networking

import retrofit2.Response
import retrofit2.http.GET

interface AppApi {
    @GET("/posts")
    suspend fun getPosts(): Response<Int>
}
