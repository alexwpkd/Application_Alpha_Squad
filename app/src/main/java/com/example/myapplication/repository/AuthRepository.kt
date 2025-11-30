package com.example.myapplication.repository

import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.remote.AuthSession
import com.example.myapplication.data.remote.RetrofitClientProducts

class AuthRepository {

    suspend fun login(correo: String, password: String): LoginResponse {
        val response = RetrofitClientProducts.authApi.login(
            LoginRequest(correo = correo, password = password)
        )

        AuthSession.token = response.token
        AuthSession.rol = response.rol
        AuthSession.idCliente = response.idCliente
        AuthSession.correo = response.correo

        return response
    }

    fun logout() {
        AuthSession.clear()
    }
}
