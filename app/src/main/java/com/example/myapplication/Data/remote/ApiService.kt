package com.example.myapplication.remote

import com.example.myapplication.Model.ProductoDto
import com.example.myapplication.Model.DetalleCarritoDto
import com.example.myapplication.Model.VentaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Query

interface ApiService {

    // ---------- PRODUCTOS ----------

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id: Long): ProductoDto

    // ---------- CARRITO / CHECKOUT ----------

    // POST /api/carritos/{idCliente}/agregar?productoId=XX&cantidad=YY
    @POST("api/carritos/{idCliente}/agregar")
    suspend fun agregarProductoAlCarrito(
        @Path("idCliente") idCliente: Long,
        @Query("productoId") productoId: Long,
        @Query("cantidad") cantidad: Int
    ): DetalleCarritoDto

    // DELETE /api/carritos/{idCliente}/vaciar
    @DELETE("api/carritos/{idCliente}/vaciar")
    suspend fun vaciarCarritoRemoto(
        @Path("idCliente") idCliente: Long
    )

    // POST /api/carritos/{idCliente}/checkout
    @POST("api/carritos/{idCliente}/checkout")
    suspend fun checkout(
        @Path("idCliente") idCliente: Long
    ): VentaDto

    @POST("api/productos")
    suspend fun crearProducto(@Body dto: ProductoCreateRequest): ProductoDto

    @POST("auth/registro/cliente")
    suspend fun registrarCliente(@Body request: ClienteRegistroRequest): ClienteResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}
