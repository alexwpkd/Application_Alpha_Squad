package com.example.myapplication.remote

import com.example.myapplication.Model.ProductoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id: Long): ProductoDto
}
