package com.example.myapplication.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.res.painterResource
import com.example.myapplication.Model.Producto
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ui.theme.ProductCard_Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCatalogScreen(
    navController: NavController,
    catalogViewModel: CatalogoViewModel,
) {
    val context = LocalContext.current
    val productos by catalogViewModel.productos.collectAsState()
    val loading by catalogViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        catalogViewModel.cargarProductos(context)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo (Admin)") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                navigationIcon = {
                    TextButton(onClick = { navController.navigate("admin") }) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos, key = { it.id }) { producto ->
                        ProductoCardAdminViewOnly(
                            producto = producto,
                            navController = navController,
                            catalogViewModel = catalogViewModel
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ProductoCardAdminViewOnly(
    producto: Producto,
    navController: NavController,
    catalogViewModel: CatalogoViewModel
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = ProductCard_Color)
    ) {
        Column(Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                val painter = if (!producto.imagenUrl.isNullOrBlank()) {
                    rememberAsyncImagePainter(producto.imagenUrl)
                } else {
                    painterResource(id = producto.imagenClave)
                }

                Image(
                    painter = painter,
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                    Text("Precio: $${producto.precio}")
                    Text(producto.descripcion, maxLines = 2)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        navController.navigate("admin/detalle/${producto.id}")
                    }
                ) {
                    Text("Ver detalle")
                }

                TextButton(
                    onClick = {
                        navController.navigate("admin/editar/${producto.id}")
                    }
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color(0xFF0E2F3A),
            title = {
                Text(
                    "Eliminar producto",
                    color = Color(0xFFEDEDED),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    "¿Estás seguro que deseas eliminar \"${producto.nombre}\"?",
                    color = Color(0xFFEDEDED)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        catalogViewModel.eliminarProducto(
                            id = producto.id.toLong(),
                            context = context
                        )
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFF5C5C)
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFEDEDED)
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

