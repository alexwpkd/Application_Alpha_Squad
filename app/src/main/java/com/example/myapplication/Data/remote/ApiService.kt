package com.example.myapplication.remote

import com.example.myapplication.Model.ProductoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // 🔐 Login contra tu backend Spring Boot
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id: Long): ProductoDto
}
