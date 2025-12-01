package com.example.myapplication.Model

import android.content.Context
import com.example.myapplication.Model.Producto   // tu modelo de UI

// DTO -> Entity (para Room)
fun ProductoDto.toEntity(): ProductoEntity =
    ProductoEntity(
        id = idProducto.toInt(),
        nombre = nombre,
        sku = sku,
        precio = precio,
        enStock = enStock,
        stock = stock,
        imagen = imagen,
        descripcion = descripcion,
        categoria = categoria,
        subcategoria = subcategoria
    )

// Entity (Room) -> Producto (tu modelo actual)
fun ProductoEntity.toDomain(context: Context): Producto {
    val drawableId = imagen?.let { img ->
        val candidate = context.resources.getIdentifier(
            img.substringBeforeLast('.'),  // "ak47.jpg" -> "ak47"
            "drawable",
            context.packageName
        )
        if (candidate != 0) candidate else android.R.drawable.ic_menu_report_image
    } ?: android.R.drawable.ic_menu_report_image

    return Producto(
        id = id,
        sku = sku,
        nombre = nombre,
        categoria = categoria,
        subcategoria = subcategoria,
        precio = precio,
        enStock = enStock,
        stock = stock,
        imagenClave = drawableId,
        descripcion = descripcion
    )
}
