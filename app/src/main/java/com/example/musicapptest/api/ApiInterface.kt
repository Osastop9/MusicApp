package com.example.musicapptest.api

import com.example.musicapptest.model.MusicItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/T021EBGU3D0-F03GZFNV7DK-26d3ad628c")
    suspend fun getResponse(): Response<MusicItem>
}
