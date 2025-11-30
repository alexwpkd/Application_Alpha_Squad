package com.example.myapplication.model

import com.example.myapplication.Model.Producto
import com.example.myapplication.data.local.ProductoEntity
import com.example.myapplication.data.model.ProductoDto

fun ProductoDto.toEntity(): ProductoEntity {
    // Toma idProducto si viene, si no id, si no 0
    val idInt = ((idProducto ?: id) ?: 0L).toInt()
    val precioInt = precio ?: 0
    val imagen = imagenUrl ?: rutaImagen

    return ProductoEntity(
        id = idInt,
        nombre = nombre,
        descripcion = descripcion,
        precio = precioInt,
        imagenUrl = imagen
    )
}

fun ProductoEntity.toProducto(): Producto {
    return Producto(
        id = this.id,
        sku = "",
        nombre = this.nombre,
        categoria = "",
        subcategoria = "",
        precio = this.precio,
        enStock = true,
        stock = 0,
        imagenClave = null,
        imagenUrl = this.imagenUrl,
        descripcion = this.descripcion ?: ""
    )
}
