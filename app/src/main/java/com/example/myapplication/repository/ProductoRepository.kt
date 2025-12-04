package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.Model.Producto
import com.example.myapplication.Model.ProductoDto
import com.example.myapplication.remote.RetrofitClient

class ProductoRepository {

    suspend fun getProductos(context: Context): List<Producto> {
        return try {
            val productosDto: List<ProductoDto> = RetrofitClient.apiService.getProductos()

            productosDto.map { dto ->
                Producto(
                    id = dto.idProducto.toInt(),
                    sku = dto.sku,
                    nombre = dto.nombre,
                    categoria = dto.categoria,
                    subcategoria = dto.subcategoria,
                    precio = dto.precio,
                    enStock = dto.enStock,
                    stock = dto.stock,
                    // 👇 Drawable genérico como fallback si no quieres usar assets
                    imagenClave = android.R.drawable.ic_menu_report_image,
                    descripcion = dto.descripcion,
                    // 👇 AQUÍ usamos la URL que viene del backend
                    imagenUrl = dto.imagen
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
