package com.wfcorp.apiapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getEndpoint(@Url url: String): Response<Any>
}
