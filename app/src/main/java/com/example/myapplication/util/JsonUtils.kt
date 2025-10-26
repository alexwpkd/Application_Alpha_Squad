package com.example.myapplication.util

import android.content.Context
import com.example.myapplication.Model.Producto
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException

fun loadProducts(context: Context): List<Producto> {
    val productos = mutableListOf<Producto>()

    try {
        val jsonString = context.assets.open("productos.json")
            .bufferedReader().use { it.readText() }

        /*val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("productos")*/
        val jsonArray = JSONArray(jsonString)


        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

<<<<<<< Updated upstream
            val nombreDrawable = obj.getString("imagenClave")
            val idDrawable = context.resources.getIdentifier(
                nombreDrawable,  // ej: "product_img_1"
                "drawable",      // carpeta res/drawable
                context.packageName
            ).let { id ->
                if (id != 0) id else android.R.drawable.ic_menu_report_image
            }

=======
>>>>>>> Stashed changes
            val producto = Producto(
                id = obj.getInt("id"),
                sku = obj.getString("sku"),
                nombre = obj.getString("nombre"),
                categoria = obj.getString("categoria"),
                subcategoria = obj.getString("subcategoria"),
                precio = obj.getInt("precio"),
                enStock = obj.getBoolean("enStock"),
                stock = obj.getInt("stock"),
                imagenClave = idDrawable,
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