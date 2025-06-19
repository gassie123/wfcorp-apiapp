package com.wfcorp.apiapp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://home.wfcorp.nl/"

    private val client = OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
        val request = chain.request().newBuilder()
            .addHeader("api-x", "F922E2307975616E4904")
            .build()
        chain.proceed(request)
    }.build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
