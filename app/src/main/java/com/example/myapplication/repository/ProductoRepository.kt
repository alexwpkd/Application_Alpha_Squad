package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.Model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonObject

class ProductoRepository {

    fun obtenerProductosDesdeAssets(context: Context, filename: String = "productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val gson = Gson()
            /*
            val listType = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson<List<Producto>>(json, listType) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
*/
            // Tu archivo tiene {"productos":[...]}
            val obj = gson.fromJson(json, JsonObject::class.java)
            val arr = obj.getAsJsonArray("productos")
            if (arr != null) {
                val listType = object : TypeToken<List<Producto>>() {}.type
                gson.fromJson<List<Producto>>(arr, listType) ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}