package com.example.myapplication.Model

data class Producto(
    val id: Int,
    val sku: String,
    val nombre: String,
    val categoria: String,
    val subcategoria: String,
    val precio: Int,
    val enStock: Boolean,
    val stock: Int,
    // imagenClave es el drawable local (opcional). imagenUrl es la URL remota (opcional).
    val imagenClave: Int? = null,
    val imagenUrl: String? = null,
    val descripcion: String
)