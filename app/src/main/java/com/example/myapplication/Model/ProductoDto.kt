package com.example.myapplication.Model

data class ProductoDto(
    val idProducto: Long,
    val nombre: String,
    val sku: String,
    val precio: Int,
    val enStock: Boolean,
    val stock: Int,
    val imagen: String?,
    val descripcion: String,
    val categoria: String,
    val subcategoria: String
)
