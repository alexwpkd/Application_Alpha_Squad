package com.example.myapplication.View

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
    var precio by remember { mutableStateOf(TextFieldValue("")) }
    var stock by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }

    var categoriaExpanded by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf("") }

    var subcategoriaExpanded by remember { mutableStateOf(false) }
    var subcategoriaSeleccionada by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    val categorias = listOf(
        "arma_primaria",
        "arma_secundaria",
        "municion",
        "accesorios"
    )

    val subcategorias = listOf(
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

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    fun productoValido(): Boolean =
        sku.text.isNotBlank() &&
                nombre.text.isNotBlank() &&
                categoriaSeleccionada.isNotBlank() &&
                subcategoriaSeleccionada.isNotBlank() &&
                precio.text.isNotBlank() &&
                stock.text.isNotBlank() &&
                descripcion.text.isNotBlank() &&
                imageUri != null

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF0E2F3A),
        unfocusedContainerColor = Color(0xFF0E2F3A),
        disabledContainerColor = Color(0xFF0E2F3A),

        focusedBorderColor = Color(0xFF6650A4),
        unfocusedBorderColor = Color(0xFF6650A4),

        focusedTextColor = Color(0xFFEDEDED),
        unfocusedTextColor = Color(0xFFEDEDED),

        focusedLabelColor = Color(0xFFEDEDED),
        unfocusedLabelColor = Color(0xFFEDEDED)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agregar producto",
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
                    text = "Completa los campos para agregar un nuevo producto",
                    fontSize = 16.sp,
                    color = Color(0xFFEDEDED)
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(sku, { sku = it }, label = { Text("SKU") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())

                // -------- CATEGORIA --------
                ExposedDropdownMenuBox(
                    expanded = categoriaExpanded,
                    onExpandedChange = { categoriaExpanded = !categoriaExpanded }
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        colors = fieldColors,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = categoriaExpanded,
                        onDismissRequest = { categoriaExpanded = false },
                        containerColor = Color(0xFF0E2F3A)
                    ) {
                        categorias.forEach {
                            DropdownMenuItem(
                                text = { Text(it, color = Color(0xFFEDEDED)) },
                                onClick = {
                                    categoriaSeleccionada = it
                                    categoriaExpanded = false
                                }
                            )
                        }
                    }
                }

                // -------- SUBCATEGORIA --------
                ExposedDropdownMenuBox(
                    expanded = subcategoriaExpanded,
                    onExpandedChange = { subcategoriaExpanded = !subcategoriaExpanded }
                ) {
                    OutlinedTextField(
                        value = subcategoriaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Subcategoría") },
                        colors = fieldColors,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = subcategoriaExpanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = subcategoriaExpanded,
                        onDismissRequest = { subcategoriaExpanded = false },
                        containerColor = Color(0xFF0E2F3A)
                    ) {
                        subcategorias.forEach {
                            DropdownMenuItem(
                                text = { Text(it, color = Color(0xFFEDEDED)) },
                                onClick = {
                                    subcategoriaSeleccionada = it
                                    subcategoriaExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(precio, { precio = it }, label = { Text("Precio (CLP)") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(stock, { stock = it }, label = { Text("Stock disponible") }, colors = fieldColors, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), maxLines = 3)

                Spacer(modifier = Modifier.height(16.dp))

                // -------- IMAGE PICKER (GALLERY ONLY) --------
                Text(
                    "Imagen del producto",
                    color = Color(0xFFEDEDED),
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEDEDED)
                    )
                ) {
                    Text("Seleccionar imagen")
                }

                imageUri?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!productoValido()) {
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
                                    precio = precio.text.toInt(),
                                    sku = sku.text.trim(),
                                    stock = stock.text.toInt(),
                                    categoria = categoriaSeleccionada,
                                    subcategoria = subcategoriaSeleccionada,
                                    descripcion = descripcion.text.trim(),
                                    imagenUrl = imageUri.toString()
                                )

                                RetrofitClient.apiService.crearProducto(dto)

                                snackbarHostState.showSnackbar("Producto agregado correctamente")
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
                    CircularProgressIndicator(color = Color(0xFF6650A4))
                }
            }
        }
    }
}
