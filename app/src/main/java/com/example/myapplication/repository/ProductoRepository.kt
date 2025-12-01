package com.example.myapplication.repository

import android.content.Context
import android.widget.Toast
import com.example.myapplication.Model.Producto
import com.example.myapplication.Model.ProductoDto
import com.example.myapplication.Model.toDomain
import com.example.myapplication.Model.toEntity
import com.example.myapplication.local.AppDatabase
import com.example.myapplication.remote.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductoRepository {

    private data class ProductoJson(
        val id: Int,
        val sku: String,
        val nombre: String,
        val categoria: String,
        val subcategoria: String,
        val precio: Int,
        val enStock: Boolean,
        val stock: Int,
        val imagenClave: String,
        val descripcion: String
    )

    // ---------- 1) ASSETS: LO QUE YA TENÍAS ----------
    fun obtenerProductosDesdeAssets(context: Context): List<Producto> {
        val jsonString =
            context.assets.open("productos.json").bufferedReader().use { it.readText() }

        val gson = Gson()
        val listProductoType = object : TypeToken<List<ProductoJson>>() {}.type
        val productosDesdeJson: List<ProductoJson> =
            gson.fromJson(jsonString, listProductoType)

        return productosDesdeJson.map { pj ->
            val idDeImagen = context.resources.getIdentifier(
                pj.imagenClave,
                "drawable",
                context.packageName
            ).let { id ->
                if (id != 0) id else android.R.drawable.ic_menu_report_image
            }

            Producto(
                id = pj.id,
                sku = pj.sku,
                nombre = pj.nombre,
                categoria = pj.categoria,
                subcategoria = pj.subcategoria,
                precio = pj.precio,
                enStock = pj.enStock,
                stock = pj.stock,
                imagenClave = idDeImagen,
                descripcion = pj.descripcion
            )
        }
    }

    // ---------- 2) API + ROOM ----------
    suspend fun obtenerProductosDesdeApi(context: Context): List<Producto> {
        val db = AppDatabase.getInstance(context)
        val dao = db.productoDao()

        val dtoList: List<ProductoDto> = RetrofitClient.apiService.getProductos()
        val entities = dtoList.map { it.toEntity() }
        dao.insertAll(entities)

        return entities.map { it.toDomain(context) }
    }

    // ---------- 3) MÉTODO PRINCIPAL QUE USA EL VIEWMODEL ----------
    suspend fun getProductos(context: Context): List<Producto> {
        return try {
            val listaApi = obtenerProductosDesdeApi(context)
            Toast.makeText(context, "Productos desde API", Toast.LENGTH_SHORT).show()
            listaApi
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Productos desde ASSETS", Toast.LENGTH_SHORT).show()
            obtenerProductosDesdeAssets(context)
        }
    }
}
