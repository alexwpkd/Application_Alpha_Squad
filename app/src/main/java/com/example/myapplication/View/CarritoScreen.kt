package com.example.myapplication.ui2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Carrito
import com.example.myapplication.ViewModel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel
) {
    val items by carritoViewModel.items.collectAsState()
    val total by carritoViewModel.total.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de compras") },
                colors = TopAppBarDefaults.topAppBarColors(),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_previous),
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío")
            }
        } else {
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
                                // Aquí más adelante puedes llamar a un endpoint de "crear venta"
                                // Por ahora solo deja el botón.
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Finalizar compra")
                        }
                    }
                }
            }
        }
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
                modifier = Modifier
                    .size(64.dp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
