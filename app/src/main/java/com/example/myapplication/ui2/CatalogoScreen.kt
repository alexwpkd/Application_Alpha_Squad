package com.example.myapplication.ui2

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Producto
import com.example.myapplication.ViewModel.CatalogoViewModel
import kotlinx.coroutines.flow.collectAsState

@Composable
fun CatalogoScreen(navController: NavController, viewModel: CatalogoViewModel) {
    val context = LocalContext.current
    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Cargar productos (solo una vez)
    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    Scaffold(topBar = {
        SmallTopAppBar(title = { Text("Catálogo") })
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Box
            }

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = productos, key = { it.id }) { producto ->
                    ProductoCard(producto = producto, onClick = {
                        navController.navigate("detalle/${producto.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val painter = rememberAsyncImagePainter(producto.imagen)
            Image(painter = painter, contentDescription = producto.nombre, modifier = Modifier.size(80.dp), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                producto.descripcion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
            }
        }
    }
}