package com.example.myapplication.ui2

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*

import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.theme.*
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Producto
import com.example.myapplication.ViewModel.CatalogoViewModel
import java.util.Locale
import kotlin.text.toInt

//import kotlinx.coroutines.flow.collectAsState
import androidx.compose.ui.res.painterResource
import com.example.myapplication.ViewModel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    catalogViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val context = LocalContext.current
    val producto by catalogViewModel.productos.collectAsState()
    val loading by catalogViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        catalogViewModel.cargarProductos(context)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Catalogo de productos\nAlpha Squad") },
            //colors = TopAppBarDefaults.topAppBarColors()
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
        )

        TextButton(onClick = { navController.navigate("home/{email}") }) {
            Text("Volver")
        }
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (producto.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(producto, key = { it.id }) { producto ->
                        ProductoCard(
                            producto = producto,
                            onDetalle = { navController.navigate("detalle/${producto.id}") }
                        )
                    }
                }
            }
        }
    }
}
@SuppressLint("DefaultLocale")
@Composable
fun ProductoCard(
    producto: Producto,
    onDetalle: () -> Unit
) {
    /*
    val context = LocalContext.current
    val imageResId = remember(producto.imagenClave) {
        context.resources.getIdentifier(producto.imagenClave, "drawable", context.packageName)
    }
     */
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = ProductCard_Color)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = producto.imagenUrl?.let { rememberAsyncImagePainter(it) }

            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                val resId = producto.imagenClave ?: android.R.drawable.ic_menu_report_image
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDetalle) {
                        Text("Ver")
                    }
                }
            }
        }
    }
}