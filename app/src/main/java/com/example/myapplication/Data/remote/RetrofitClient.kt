package com.example.myapplication.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 👉 EXPLICACIÓN:
    // - Si tu backend corre en tu PC y pruebas en el EMULADOR de Android Studio:
    //     usa "http://10.0.2.2:8080/"
    //   (10.0.2.2 es "localhost" visto desde el emulador).
    //
    // - Si pruebas en un CELULAR FÍSICO en la misma red WiFi:
    //     cambia esto por la IP de tu PC, por ejemplo:
    //     "http://192.168.1.10:8080/"
    //
    // - Si tu backend está desplegado en internet:
    //     pon la URL pública, por ejemplo:
    //     "https://mi-dominio.com/"
    //
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
