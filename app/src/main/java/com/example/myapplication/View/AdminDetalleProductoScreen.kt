package com.example.myapplication.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Producto
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ui.theme.ProductCard_Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDetalleProductoScreen(
    navController: NavController,
    catalogViewModel: CatalogoViewModel,
    productoId: Long
) {
    val context = LocalContext.current
    var producto by remember { mutableStateOf<Producto?>(null) }
    val loading by catalogViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        catalogViewModel.cargarProductos(context)
        producto = catalogViewModel.productos.value.find { it.id.toLong() == productoId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Volver", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF051922))
            )
        }
    ) { padding ->
        if (loading || producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFF051922)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val painter = if (!producto!!.imagenUrl.isNullOrBlank()) {
                    rememberAsyncImagePainter(producto!!.imagenUrl)
                } else {
                    painterResource(id = producto!!.imagenClave)
                }

                Image(
                    painter = painter,
                    contentDescription = producto!!.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(ProductCard_Color, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Text("Nombre: ${producto!!.nombre}", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("SKU: ${producto!!.sku}", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Categoría: ${producto!!.categoria}", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Subcategoría: ${producto!!.subcategoria}", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Precio: $${producto!!.precio}", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Stock disponible: ${producto!!.stock}", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Descripción: ${producto!!.descripcion}", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate("admin/editar/${producto!!.id}")
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text("Editar Producto")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
