package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(email: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido 👋", style = MaterialTheme.typography.titleLarge)
        Text("Has iniciado sesión como: $email")
    }
}