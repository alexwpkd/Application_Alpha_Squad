package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.Model.Producto
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.ProductoEntity
import com.example.myapplication.data.model.ProductoDto
import com.example.myapplication.data.remote.RetrofitClientProducts
import com.example.myapplication.model.toEntity
import com.example.myapplication.model.toProducto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductoRepository {

    // Intenta traer datos desde la API; en caso de error devuelve datos de Room o assets
    suspend fun getProductos(context: Context): List<Producto> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()
        return try {
            val dtoList: List<ProductoDto> = RetrofitClientProducts.productsApi.getProductos()
            val entities: List<ProductoEntity> = dtoList.map { it.toEntity() }
            dao.insertAll(entities)
            entities.map { it.toProducto() }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: datos en local (puede ser empty)
            val cached = dao.getAll()
            if (cached.isNotEmpty()) cached.map { it.toProducto() }
            else {
                // Último recurso: cargar desde assets si existe
                cargarDesdeAssets(context)
            }
        }
    }

    suspend fun getProductoPorId(context: Context, id: Int): Producto? {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()
        val fromDb = dao.findById(id)
        if (fromDb != null) return fromDb.toProducto()
        return try {
            val dto = RetrofitClientProducts.productsApi.getProductoPorId(id)
            val entity = dto.toEntity()
            dao.insertAll(listOf(entity))
            entity.toProducto()
        } catch (_: Exception) {
            null
        }
    }

    private fun cargarDesdeAssets(context: Context): List<Producto> {
        return try {
            val jsonString = context.assets.open("productos.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<List<Producto>>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}