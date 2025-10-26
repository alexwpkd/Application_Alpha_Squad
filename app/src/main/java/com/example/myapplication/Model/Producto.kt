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
    val imagenClave: String,
    val descripcion: String
)