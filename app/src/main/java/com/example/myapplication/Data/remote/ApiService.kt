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

    // 🧑‍💼 Registro de cliente
    @POST("auth/registro/cliente")
    suspend fun registrarCliente(@Body request: ClienteRegistroRequest): ClienteResponse

    // 🧱 Catálogo de productos (público)
    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id: Long): ProductoDto

    // 🛠 Crear producto (requiere rol ADMIN o EMPLEADO)
    @POST("api/productos")
    suspend fun crearProducto(@Body dto: ProductoCreateRequest): ProductoDto
}
