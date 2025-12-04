package com.example.myapplication.remote

data class LoginResponse(
    val token: String,
    val rol: String,
    val idCliente: Long?,
    val correo: String
)
