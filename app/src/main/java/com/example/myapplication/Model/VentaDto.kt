package com.example.myapplication.Model

data class VentaDto(
    val idVenta: Long,
    val fechaVenta: String,
    val total: Int,
    val descuento: Int,
    val estado: String
)
