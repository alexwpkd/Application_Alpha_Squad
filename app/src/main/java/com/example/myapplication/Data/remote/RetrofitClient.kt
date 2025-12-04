package com.example.myapplication.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://54.87.10.251:8080/"

    // Guardamos el token JWT que devuelve el backend
    @Volatile
    private var authToken: String? = null

    fun setToken(token: String?) {
        authToken = token
    }

    // Cliente HTTP con interceptor que agrega el header Authorization si hay token
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                authToken?.let { token ->
                    builder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(builder.build())
            }
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
