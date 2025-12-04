package com.example.myapplication.remote

data class ClienteResponse(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val rut: String,
    val correo: String,
    val direccion: String
)
