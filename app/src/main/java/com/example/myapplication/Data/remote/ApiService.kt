package com.example.myapplication.remote

import com.example.myapplication.Model.ProductoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/registro/cliente")
    suspend fun registrarCliente(@Body request: ClienteRegistroRequest): ClienteResponse

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id: Long): ProductoDto

    @POST("api/productos")
    suspend fun crearProducto(@Body dto: ProductoCreateRequest): ProductoDto
}
