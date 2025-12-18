package com.example.myapplication.View

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.remote.ProductoCreateRequest
import com.example.myapplication.remote.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun administradorEditarProducto(
    navController: NavController,
    catalogViewModel: CatalogoViewModel,
    productoId: Long
) {
    var sku by remember { mutableStateOf(TextFieldValue("")) }
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var categoria by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }
    var stock by remember { mutableStateOf(TextFieldValue("")) }
    var imagenClave by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }

    val opciones = listOf(
        "fusil",
        "subfusil",
        "pistola",
        "bbs",
        "co2",
        "optica",
        "agarres",
        "correas",
        "iluminacion"
    )

    val categorias = listOf(
        "arma_primaria",
        "arma_secundaria",
        "municion",
        "accesorios"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    var categoriaExpanded by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(productoId) {
        loading = true
        try {
            val producto = RetrofitClient.apiService.getProductoPorId(productoId)
            sku = TextFieldValue(producto.sku)
            nombre = TextFieldValue(producto.nombre)
            precio = TextFieldValue(producto.precio.toString())
            stock = TextFieldValue(producto.stock.toString())
            descripcion = TextFieldValue(producto.descripcion)
            imagenClave = TextFieldValue(producto.imagen ?: "")
            categoriaSeleccionada = producto.categoria
            selectedText = producto.subcategoria
        } catch (e: Exception) {
            snackbarHostState.showSnackbar("Error cargando producto: ${e.message}")
        } finally {
            loading = false
        }
    }

    fun productoValido(): Boolean =
        sku.text.isNotBlank() &&
                nombre.text.isNotBlank() &&
                categoriaSeleccionada.isNotBlank() &&
                precio.text.toIntOrNull()?.let { it >= 0 } == true &&
                stock.text.toIntOrNull()?.let { it >= 0 } == true &&
                imagenClave.text.isNotBlank() &&
                descripcion.text.isNotBlank() &&
                selectedText.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar producto",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEDEDED)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF051922)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF051922))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedButton(
                    onClick = { navController.navigate("admin") },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Volver")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Edita los campos del producto",
                    fontSize = 16.sp,
                    color = Color(0xFFEDEDED)
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = sku,
                    onValueChange = { sku = it },
                    label = { Text("SKU") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0E2F3A),
                        unfocusedContainerColor = Color(0xFF0E2F3A),
                        focusedBorderColor = Color(0xFF6650A4),
                        unfocusedBorderColor = Color(0xFF6650A4),
                        focusedTextColor = Color(0xFFEDEDED),
                        unfocusedTextColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio (CLP)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0E2F3A),
                        unfocusedContainerColor = Color(0xFF0E2F3A),
                        focusedBorderColor = Color(0xFF6650A4),
                        unfocusedBorderColor = Color(0xFF6650A4),
                        focusedTextColor = Color(0xFFEDEDED),
                        unfocusedTextColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock disponible") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0E2F3A),
                        unfocusedContainerColor = Color(0xFF0E2F3A),
                        focusedBorderColor = Color(0xFF6650A4),
                        unfocusedBorderColor = Color(0xFF6650A4),
                        focusedTextColor = Color(0xFFEDEDED),
                        unfocusedTextColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = imagenClave,
                    onValueChange = { imagenClave = it },
                    label = { Text("URL de imagen") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0E2F3A),
                        unfocusedContainerColor = Color(0xFF0E2F3A),
                        focusedBorderColor = Color(0xFF6650A4),
                        unfocusedBorderColor = Color(0xFF6650A4),
                        focusedTextColor = Color(0xFFEDEDED),
                        unfocusedTextColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0E2F3A),
                        unfocusedContainerColor = Color(0xFF0E2F3A),
                        focusedBorderColor = Color(0xFF6650A4),
                        unfocusedBorderColor = Color(0xFF6650A4),
                        focusedTextColor = Color(0xFFEDEDED),
                        unfocusedTextColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!productoValido()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("ERROR: Campos incompletos o inválidos")
                            }
                            return@Button
                        }

                        scope.launch {
                            try {
                                loading = true
                                RetrofitClient.apiService.actualizarProducto(
                                    id = productoId,
                                    dto = ProductoCreateRequest(
                                        nombre = nombre.text.trim(),
                                        precio = precio.text.toInt(),
                                        sku = sku.text.trim(),
                                        stock = stock.text.toInt(),
                                        categoria = categoriaSeleccionada.trim(),
                                        subcategoria = selectedText.trim(),
                                        descripcion = descripcion.text.trim(),
                                        imagenUrl = imagenClave.text.trim()
                                    )
                                )
                                snackbarHostState.showSnackbar("Producto actualizado correctamente")
                                delay(1500)
                                navController.navigate("admin")
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Error: ${e.message}")
                            } finally {
                                loading = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6650A4),
                        contentColor = Color(0xFFEDEDED)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar Cambios", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6650A4))
                }
            }
        }
    }
}
