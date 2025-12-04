package com.example.myapplication.remote

data class ClienteRegistroRequest(
    val nombre: String,
    val apellidos: String,
    val rut: String,
    val correo: String,
    val password: String,
    val direccion: String
)
