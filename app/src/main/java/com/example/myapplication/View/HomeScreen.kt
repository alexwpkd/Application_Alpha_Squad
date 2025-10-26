package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import kotlinx.coroutines.*
import androidx.compose.runtime.*
@Composable
fun HomeScreen(
    navController: NavController,
    email: String?
)

{
    var welcome by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        welcome = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    )

    {
        if (welcome == true) {
            Text("Bienvenido 👋", style = MaterialTheme.typography.titleLarge)
            Text("Has iniciado sesión como: $email")
        } else {
            TextButton(onClick = { navController.navigate("cart") }) {
                Text("Entrar a carrito")
            }

            TextButton(onClick = { navController.navigate("catalogue") }) {
                Text("Entrar a catalogo")
            }
        }

    }
}