package com.example.myapplication.View

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable

fun CarritoScreen(navController: NavController) {
    Column()

    {
        Text("Carrito de compras")
    }

    TextButton(onClick = { navController.navigate("home/{email}") }) {
        Text("Volver")
    }
}