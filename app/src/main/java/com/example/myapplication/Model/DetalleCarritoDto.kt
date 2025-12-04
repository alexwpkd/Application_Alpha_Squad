package com.example.myapplication.Model

data class DetalleCarritoDto(
    val idCarritoDetalle: Long,
    val cantidad: Int,
    val producto: ProductoDto
)
