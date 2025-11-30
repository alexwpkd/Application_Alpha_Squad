package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO que representa la respuesta de tu backend en /api/productos.
 * Soporta idProducto o id, e imagenUrl o rutaImagen.
 */
data class ProductoDto(

    @SerializedName("idProducto")
    val idProducto: Long? = null,

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("precio")
    val precio: Int? = null,

    @SerializedName("sku")
    val sku: String? = null,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("categoria")
    val categoria: String? = null,

    @SerializedName("subcategoria")
    val subcategoria: String? = null,

    @SerializedName("imagenUrl")
    val imagenUrl: String? = null,

    @SerializedName("rutaImagen")
    val rutaImagen: String? = null
)
