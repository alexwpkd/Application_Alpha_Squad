package com.example.myapplication.View

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.remote.ProductoCreateRequest
import com.example.myapplication.remote.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun administradorAgregarProducto(
    navController: NavController
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

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(false) }

    fun productoAgregado(): Boolean {
        return (
                        sku.text.isNotBlank() &&
                        nombre.text.isNotBlank() &&
                        categoria.text.isNotBlank() &&
                        precio.text.isNotBlank() &&
                        stock.text.isNotBlank() &&
                        imagenClave.text.isNotBlank() &&
                        descripcion.text.isNotBlank() &&
                        selectedText.isNotBlank()
                )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Agregar producto", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("admin") },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Volver")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Completa los campos para agregar un nuevo producto",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = sku, onValueChange = { sku = it },
                    label = { Text("SKU") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = categoria, onValueChange = { categoria = it },
                    label = { Text("Categoria") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = {},
                        label = { Text("Subcategoria") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion, color = Color.Black) },
                                onClick = {
                                    selectedText = opcion
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = precio, onValueChange = { precio = it },
                    label = { Text("Precio (CLP)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stock, onValueChange = { stock = it },
                    label = { Text("Stock disponible") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = imagenClave, onValueChange = { imagenClave = it },
                    label = { Text("URL de imagen") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion, onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!productoAgregado()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("ERROR: Campos incompletos")
                            }
                            return@Button
                        }

                        scope.launch {
                            try {
                                loading = true

                                val dto = ProductoCreateRequest(
                                    nombre = nombre.text.trim(),
                                    precio = precio.text.toIntOrNull() ?: 0,
                                    sku = sku.text.trim(),
                                    stock = stock.text.toIntOrNull() ?: 0,
                                    categoria = categoria.text.trim(),
                                    subcategoria = selectedText.trim(),
                                    descripcion = descripcion.text.trim(),
                                    imagenUrl = imagenClave.text.trim()
                                )

                                RetrofitClient.apiService.crearProducto(dto)

                                snackbarHostState.showSnackbar("Producto agregado en el servidor")
                                delay(1500)
                                navController.navigate("admin")
                            } catch (e: Exception) {
                                e.printStackTrace()
                                snackbarHostState.showSnackbar("Error al crear producto: ${e.message}")
                            } finally {
                                loading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Agregar Producto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
