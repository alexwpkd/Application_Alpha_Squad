package com.example.myapplication.ui2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.Model.Carrito
import com.example.myapplication.Model.Producto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoViewModel: CarritoViewModel,
    navController: NavController,
) {
    val items by carritoViewModel.carrito.collectAsState()
    val total by carritoViewModel.total.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Carrito")
                },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            navController.navigate("home/{email}")
                        }
                    ) {
                        Text("Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.producto.id }) { item ->
                    CarritoItemRow(
                        item = item,
                        onIncrementar = {
                            carritoViewModel.actualizarCantidad(
                                item.producto.id,
                                item.cantidad + 1
                            )
                        },
                        onDecrementar = {
                            carritoViewModel.actualizarCantidad(
                                item.producto.id,
                                item.cantidad - 1
                            )
                        },
                        onEliminar = {
                            carritoViewModel.eliminarDelCarrito(item.producto.id)
                        }
                    )
                }
            }

            Divider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Total: $$total",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { carritoViewModel.vaciarCarrito() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vaciar carrito")
                    }

                    Button(
                        onClick = {
                            if (items.isEmpty()) {
                                mostrarError = true
                            } else {
                                mostrarDialogo = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Finalizar compra")
                    }


                }
            }
        }
    }

    if (mostrarError) {
        AlertDialog(
            onDismissRequest = { mostrarError = false },
            containerColor = Color(0xFF0E2F3A),
            title = {
                Text(
                    "Carrito vacío",
                    color = Color(0xFFEDEDED),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    "No puedes realizar una compra sin productos.",
                    color = Color(0xFFEDEDED)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { mostrarError = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEDEDED))
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            containerColor = Color(0xFF0E2F3A),
            title = {
                Text(
                    "Confirmar compra",
                    color = Color(0xFFEDEDED),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    "¿Deseas finalizar tu compra?",
                    color = Color(0xFFEDEDED)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        carritoViewModel.realizarCompra(1L) { exito, mensaje ->
                            if (exito) {
                                mostrarDialogo = false
                                navController.navigate("home/{email}")
                            }
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50))
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogo = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEDEDED))
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


}

@Composable
private fun CarritoItemRow(
    item: Carrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = if (!item.producto.imagenUrl.isNullOrBlank()) {
                rememberAsyncImagePainter(item.producto.imagenUrl)
            } else {
                painterResource(id = item.producto.imagenClave)
            }

            Image(
                painter = painter,
                contentDescription = item.producto.nombre,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${item.producto.precio}")
                Text("Subtotal: $${item.producto.precio * item.cantidad}")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrementar) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_previous),
                            contentDescription = "Menos"
                        )
                    }

                    Text(item.cantidad.toString())

                    IconButton(onClick = onIncrementar) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_input_add),
                            contentDescription = "Más"
                        )
                    }
                }

                TextButton(onClick = onEliminar) {
                    Text("Eliminar")
                }
            }
        }
    }
}