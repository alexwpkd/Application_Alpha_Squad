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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(navController: NavController, catalogViewModel: CatalogoViewModel) {
    val context = LocalContext.current
    val producto by catalogViewModel.productos.collectAsState()
    val loading by catalogViewModel.loading.collectAsState()

    // Cargar productos (solo una vez)
    LaunchedEffect(Unit) {
        catalogViewModel.cargarProductos(context)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Bienvenido a Alpha Squad") },
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
                //return@Box
            }else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = producto, key = { it.id }) { producto ->
                        ProductoCard(producto = producto, onClick = {
                            navController.navigate("detalle/${producto.id}")
                        })
                    }
                }
            }

        }
    }
}
@SuppressLint("DefaultLocale")
@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    /*
    val context = LocalContext.current
    val imageResId = remember(producto.imagenClave) {
        context.resources.getIdentifier(producto.imagenClave, "drawable", context.packageName)
    }
     */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = ProductCard_Color)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                //painter = rememberAsyncImagePainter(imageResId),
                painter = painterResource(id = producto.imagenClave),
                contentDescription = producto.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Precio: $${String.format(Locale("es", "CL"), "%,d", producto.precio.toInt())}",
                    style = MaterialTheme.typography.bodyMedium
                )
                producto.descripcion?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
            }
        }
    }
}