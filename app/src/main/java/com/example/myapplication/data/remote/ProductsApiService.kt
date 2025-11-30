package com.example.myapplication.data.remote

import com.example.myapplication.data.model.ProductoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsApiService {

    // GET /api/productos
    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    // GET /api/productos/{id}
    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id: Int): ProductoDto

    // Opcional: por categoría
    @GET("api/productos/categoria/{categoria}")
    suspend fun getProductosPorCategoria(@Path("categoria") categoria: String): List<ProductoDto>

    // Opcional: por subcategoría
    @GET("api/productos/subcategoria/{subcategoria}")
    suspend fun getProductosPorSubcategoria(@Path("subcategoria") subcategoria: String): List<ProductoDto>
}
