package com.example.myapplication.remote

data class ProductoCreateRequest(
    val nombre: String,
    val precio: Int,
    val sku: String,
    val stock: Int,
    val categoria: String,
    val subcategoria: String,
    val descripcion: String,
    val imagenUrl: String
)
