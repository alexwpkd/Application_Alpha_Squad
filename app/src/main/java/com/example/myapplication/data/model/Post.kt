package com.example.myapplication.data.model

// Esta clase representa un post obtenido desde la API
data class Post(
    val userId: Int,  // ID del usuario que creó el post
    val id: Int,      // ID del post
    val title: String, // Titulo del post
    val body: String   // Cuerpo o contenido del post
)
