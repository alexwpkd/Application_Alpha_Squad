package com.example.myapplication.data.remote

/**
 * Mantiene la sesión actual del usuario logueado.
 * Se llena después de llamar a /auth/login.
 */
object AuthSession {
    var token: String? = null
    var rol: String? = null
    var idCliente: Long? = null
    var correo: String? = null

    fun clear() {
        token = null
        rol = null
        idCliente = null
        correo = null
    }
}
