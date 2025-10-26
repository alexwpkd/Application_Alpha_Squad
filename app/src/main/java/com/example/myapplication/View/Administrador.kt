package com.example.myapplication.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun administrador(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = { navController.navigate("adminCatalogue") }) {
            Text("Ir a catalogo de productos")
        }

        TextButton(onClick = { navController.navigate("adminProductAdd") }) {
            Text("Ir a agregar producto")
        }

        TextButton(onClick = { navController.navigate("home/{email}") }) {
            Text("Volver a home")
        }
    }
}