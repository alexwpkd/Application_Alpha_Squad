package com.example.myapplication.ui2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ViewModel.CatalogoViewModel
import androidx.compose.foundation.layout.Arrangement

//
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(productoId: Int, viewModel: CatalogoViewModel,navController: NavController) {
    val context = LocalContext.current

    // Si entras directo al detalle, asegura que los productos estén cargados
    LaunchedEffect(Unit) {
        if (viewModel.productos.value.isEmpty()) {
            viewModel.cargarProductos(context)
        }
    }

    val productos by viewModel.productos.collectAsState()
    val producto = remember(productos, productoId) {
        productos.find { it.id == productoId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = producto?.nombre ?: "Detalle") },
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
        producto?.let { p ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Simplificado: Se confía en que imagenClave es un ID de drawable válido
                // gracias a la lógica de fallback en el repositor
                Image(
                    painter = painterResource(id = p.imagenClave),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Text(text = p.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = "$${p.precio}", style = MaterialTheme.typography.titleMedium)
                Text(text = p.descripcion, style = MaterialTheme.typography.bodyMedium)

                Button(
                    onClick = { /* TODO: agregar al carrito */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
                OutlinedButton(
                    onClick = { navController.navigate("catalogue") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Regresar a la tienda")
                }
            }
        }

    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Producto no encontrado")
    }
}