package com.example.myapplication.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
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
