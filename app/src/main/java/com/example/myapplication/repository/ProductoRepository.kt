package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.Model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductoRepository {

    fun obtenerProductosDesdeAssets(context: Context, filename: String = "productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson<List<Producto>>(json, listType) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}