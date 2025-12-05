package com.example.myapplication.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private const val BASE_URL = "http://54.198.242.155:8080/"


    @Volatile
    private var token: String? = null

    fun setToken(newToken: String?) {
        token = newToken
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()


                token?.let {
                    builder.addHeader("Authorization", "Bearer $it")
                }

                builder.addHeader("Content-Type", "application/json")
                chain.proceed(builder.build())
            }
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
