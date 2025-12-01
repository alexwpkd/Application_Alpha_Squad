package com.example.myapplication.View

import androidx.compose.foundation.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ViewModel.CarritoViewModel
//

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold

@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel
) {
    val productos by carritoViewModel.carrito.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Carrito de compras", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(15.dp))

            TextButton(onClick = { navController.navigate("home/{email}") }) {
                Text("Volver")
            }

            TextButton(onClick = { navController.navigate("catalogue") }) {
                Text("Ir a catalogo")
            }

            if (productos.isEmpty()) {
                Text("El carrito está vacío")
            } else {
                LazyColumn {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Precio: $${producto.precio}")
                                Text(producto.descripcion ?: "")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (productos.isEmpty()) {
                            snackbarHostState.showSnackbar("ERROR: No se pudo realizar la compra")
                        } else {
                            val ok = carritoViewModel.realizarCompra()
                            if (ok) {
                                snackbarHostState.showSnackbar("Compra realizada con exito")
                            } else {
                                snackbarHostState.showSnackbar("ERROR: No se pudo realizar la compra")
                            }
                        }
                    }
                },
                enabled = productos.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comprar")
            }

            Button(
                onClick = {
                    scope.launch {
                        val ok = carritoViewModel.realizarCompra()
                        if (ok) {
                            snackbarHostState.showSnackbar("Carrito eliminado")
                        }
                    }
                },
                enabled = productos.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar carrito")
            }

        }
    }
}