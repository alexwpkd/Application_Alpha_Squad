package com.example.myapplication.data.model

data class LoginResponse(
    val token: String,
    val rol: String,
    val idCliente: Long?,
    val correo: String
)
