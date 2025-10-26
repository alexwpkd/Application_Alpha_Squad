package com.example.myapplication.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Producto
import com.example.myapplication.util.loadProducts

@Composable
fun administradorCatalogo(
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

        TextButton(onClick = { navController.navigate("admin") }) {
            Text("Volver")
        }

        if (productos.isEmpty()) {
            Text("No hay productos")
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
                        val painter = rememberAsyncImagePainter(producto.imagenClave)
                        Image(painter = painter, contentDescription = producto.nombre, modifier = Modifier.size(80.dp), contentScale = ContentScale.Crop)
                    }
                }
                }
            }
        }
    }
}