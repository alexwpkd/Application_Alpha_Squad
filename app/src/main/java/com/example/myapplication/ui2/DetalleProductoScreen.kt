package com.example.myapplication.ui2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.ui.theme.ProductCard_Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(productoId) {
        if (viewModel.productos.value.isEmpty()) {
            viewModel.cargarProductos(context)
        }
    }

    val producto = remember(productos, productoId) {
        productos.find { it.id == productoId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = producto?.nombre ?: "Detalle del Producto") },
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->

        if (loading && producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Producto no encontrado")
            }
            return@Scaffold
        }

        val p = producto!!

        val painter = if (!p.imagenUrl.isNullOrBlank()) {
            rememberAsyncImagePainter(p.imagenUrl)
        } else {
            painterResource(id = p.imagenClave)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painter,
                contentDescription = p.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ BLOQUE DE DETALLES (igual que admin, pero en UI usuario)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ProductCard_Color),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Nombre: ${p.nombre}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "SKU: ${p.sku}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Categoría: ${p.categoria}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Subcategoría: ${p.subcategoria}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Precio: $${p.precio}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (p.enStock) "Stock disponible: ${p.stock}" else "Stock disponible: 0 (Sin stock)",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Descripción: ${p.descripcion}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Acciones (se mantienen)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        if (!p.enStock || p.stock <= 0) {
                            scope.launch {
                                snackbarHostState.showSnackbar("No hay stock disponible")
                            }
                        } else {
                            carritoViewModel.agregarAlCarrito(p)
                            scope.launch {
                                snackbarHostState.showSnackbar("Producto agregado al carrito")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }

                OutlinedButton(
                    onClick = { navController.navigate("carrito") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a carrito de compras")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
