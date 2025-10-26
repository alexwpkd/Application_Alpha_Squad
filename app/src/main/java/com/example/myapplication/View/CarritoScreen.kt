package com.example.myapplication.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Model.Producto
import com.example.myapplication.util.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun CarritoScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }

    LaunchedEffect(Unit) {
        productos = loadProducts(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Carrito de compras")
        Spacer(modifier = Modifier.height(15.dp))

        TextButton(onClick = { navController.navigate("home/{email}") }) {
            Text("Volver")
        }

        if (productos.isEmpty()) {
            Text("No hay productos disponibles por el momento")
        } else {
            LazyColumn {
                items(productos) {
                        producto -> Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: $${producto.precio}")
                        Text(producto.descripcion)
                    }
                }
                }
            }
        }
    }
}