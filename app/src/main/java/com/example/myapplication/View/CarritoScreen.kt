package com.example.myapplication.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable

fun CarritoScreen(
    navController: NavController
)

{
    Column()

    {
        Text("Carrito de compras")
        Spacer(modifier = Modifier.height(15.dp))
    }

    TextButton(onClick = { navController.navigate("home/{email}") }) {
        Text("Volver")
    }
}