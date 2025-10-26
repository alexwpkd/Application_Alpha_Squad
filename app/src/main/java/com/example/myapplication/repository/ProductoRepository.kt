package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.Model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonObject

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
        val imagenClave: String, // <-- Aquí se mantiene como String
        val descripcion: String
    )
    /*
    fun obtenerProductosDesdeAssets(context: Context, filename: String = "productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }*/

    fun obtenerProductosDesdeAssets(context: Context): List<Producto> {
        val jsonString = context.assets.open("productos.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val listProductoType = object : TypeToken<List<ProductoJson>>() {}.type
        val productosDesdeJson: List<ProductoJson> = gson.fromJson(jsonString, listProductoType)

        return productosDesdeJson.map { pj ->
            // Convierte el nombre del drawable a su ID Int
            val idDeImagen = context.resources.getIdentifier(
                pj.imagenClave,        // ej: "product_img_1"
                "drawable",            // carpeta res/drawable
                context.packageName
            ).let { id ->
                if (id != 0) id else android.R.drawable.ic_menu_report_image // fallback seguro
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
                imagenClave = idDeImagen,  // <-- ahora es Int
                descripcion = pj.descripcion
            )
        }
    }
}