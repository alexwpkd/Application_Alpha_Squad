package com.example.myapplication.ui2
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ViewModel.CarrionViewModel
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarrionViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { padding ->
        producto?.let { p ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                val painter = if (!p.imagenUrl.isNullOrBlank()) {
                    rememberAsyncImagePainter(p.imagenUrl)
                } else {
                    painterResource(id = p.imagenClave)
                }

                Image(
                    painter = painter,
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
                    onClick = {
                        carritoViewModel.agregarAlCarrito(p)
                        scope.launch {
                            snackbarHostState.showSnackbar("Producto agregado")
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
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Producto no encontrado")
        }
    }
}
