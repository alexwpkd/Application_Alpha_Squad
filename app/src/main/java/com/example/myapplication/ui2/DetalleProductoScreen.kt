package com.example.myapplication.ui2
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ViewModel.CatalogoViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(productoId: Int, ViewModel: CatalogoViewModel) {
    val producto = remember { ViewModel.buscarProductoPorId(productoId) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = producto?.nombre ?: "Detalle") },
            colors = TopAppBarDefaults.topAppBarColors()
        )

    }) { padding ->
        producto?.let { p ->
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val painter = rememberAsyncImagePainter(p.imagenClave)
                Image(painter = painter, contentDescription = p.nombre, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp))
                Text(text = p.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = "$${p.precio}", style = MaterialTheme.typography.titleMedium)
                Text(text = p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { /* TODO: agregar al carrito */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Agregar al carrito")
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado")
        }
    }
}