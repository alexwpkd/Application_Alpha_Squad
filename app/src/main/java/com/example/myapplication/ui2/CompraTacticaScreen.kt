package com.example.myapplication.ui2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Producto
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.ViewModel.CompraTacticaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraTacticaScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    tacticaViewModel: CompraTacticaViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (catalogoViewModel.productos.value.isEmpty()) {
            catalogoViewModel.cargarProductos(context)
        }
    }

    val productos by catalogoViewModel.productos.collectAsState()
    val loading by catalogoViewModel.loading.collectAsState()
    val seleccion by tacticaViewModel.seleccion.collectAsState()

    // Tabs por categoria (tipo menú rápido)
    val categorias = remember(productos) {
        listOf("Todo") + productos.map { it.categoria }.distinct()
    }
    var tabIndex by remember { mutableStateOf(0) }
    val categoriaActual = categorias.getOrNull(tabIndex) ?: "Todo"

    val productosFiltrados = remember(productos, categoriaActual) {
        if (categoriaActual == "Todo") productos
        else productos.filter { it.categoria == categoriaActual }
    }

    val totalSeleccionados = seleccion.values.sum()
    val totalPrecio = seleccion.entries.sumOf { (id, cant) ->
        val p = productos.firstOrNull { it.id == id }
        (p?.precio ?: 0) * cant
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compra táctica") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_previous),
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { tacticaViewModel.limpiar() }) {
                        Text("Limpiar")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Seleccionados: $totalSeleccionados")
                        Text("Total: $$totalPrecio")
                    }

                    Button(
                        onClick = {
                            // Traspaso al carrito normal
                            seleccion.forEach { (id, cant) ->
                                val p = productos.firstOrNull { it.id == id }
                                if (p != null) carritoViewModel.agregarAlCarrito(p, cant)
                            }
                            tacticaViewModel.limpiar()
                            navController.navigate("carrito")
                        },
                        enabled = totalSeleccionados > 0
                    ) {
                        Text("Agregar al carrito")
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ScrollableTabRow(selectedTabIndex = tabIndex) {
                categorias.forEachIndexed { index, cat ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(cat) }
                    )
                }
            }

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
                    items(productosFiltrados, key = { it.id }) { p ->
                        CompraTacticaItem(
                            producto = p,
                            cantidadSeleccionada = seleccion[p.id] ?: 0,
                            onMas = {
                                if (p.enStock && p.stock > 0) tacticaViewModel.incrementar(p.id, p.stock)
                            },
                            onMenos = { tacticaViewModel.decrementar(p.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompraTacticaItem(
    producto: Producto,
    cantidadSeleccionada: Int,
    onMas: () -> Unit,
    onMenos: () -> Unit
) {
    val disponible = producto.enStock && producto.stock > 0

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = if (!producto.imagenUrl.isNullOrBlank()) {
                rememberAsyncImagePainter(producto.imagenUrl)
            } else {
                painterResource(id = producto.imagenClave)
            }

            Image(
                painter = painter,
                contentDescription = producto.nombre,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("$$${producto.precio}")
                Text(
                    if (disponible) "Stock: ${producto.stock}" else "Sin stock",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenos, enabled = cantidadSeleccionada > 0) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_media_previous),
                        contentDescription = "Menos"
                    )
                }

                Text(cantidadSeleccionada.toString(), modifier = Modifier.width(24.dp))

                IconButton(onClick = onMas, enabled = disponible && cantidadSeleccionada < producto.stock) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Más"
                    )
                }
            }
        }
    }
}
