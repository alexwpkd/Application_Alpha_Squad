package com.example.myapplication.Model

object FakeDatabase {
    private val usuarios = mutableListOf<Usuario>()

    fun registrar(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario)
        return true
    }

    fun login(email: String, password: String): Boolean {
        return usuarios.any { it.email == email && it.password == password }
    }
}