package com.example.myapplication.util

import android.content.Context
import com.example.myapplication.Model.Producto
import org.json.JSONObject
import java.io.IOException

fun loadProducts(context: Context): List<Producto> {
    val productos = mutableListOf<Producto>()

    try {
        val jsonString = context.assets.open("productos.json")
            .bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonString)

        val jsonArray = jsonObject.getJSONArray("productos")

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val producto = Producto(
                id = obj.getInt("id"),
                sku = obj.getString("sku"),
                nombre = obj.getString("nombre"),
                categoria = obj.getString("categoria"),
                subcategoria = obj.getString("subcategoria"),
                precio = obj.getInt("precio"),
                enStock = obj.getBoolean("enStock"),
                stock = obj.getInt("stock"),
                imagenClave = obj.getString("imagenClave"),
                descripcion = obj.getString("descripcion")
            )
            productos.add(producto)
        }

    } catch (e: IOException) {
        e.printStackTrace() //404
    } catch (e: Exception) {
        e.printStackTrace() //json
    }


return productos
}