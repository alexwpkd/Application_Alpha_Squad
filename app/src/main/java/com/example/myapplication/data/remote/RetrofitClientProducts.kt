package com.example.myapplication.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientProducts {

    // BACKEND ALPHA SQUAD
    // Desde el emulador Android, localhost:8080 = 10.0.2.2:8080
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Interceptor para agregar Authorization: Bearer <token> si existe sesión
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()

        AuthSession.token?.let { token ->
            builder.header("Authorization", "Bearer $token")
        }

        chain.proceed(builder.build())
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Productos
    val productsApi: ProductsApiService by lazy {
        retrofit.create(ProductsApiService::class.java)
    }

    // Autenticación
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}
