package com.example.myapplication.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.isPopupLayout
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun administradorAgregarProducto(
    navController: NavController
) {

    var id by remember { mutableStateOf(TextFieldValue("")) }
    var sku by remember { mutableStateOf(TextFieldValue("")) }
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var categoria by remember { mutableStateOf(TextFieldValue("")) }
    var subcategoria by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }
    var stock by remember { mutableStateOf(TextFieldValue("")) }
    var imagenClave by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }

    fun producto_agregado() : Boolean {
        if (
                id.text.isNotBlank() &&
                sku.text.isNotBlank() &&
                nombre.text.isNotBlank() &&
                categoria.text.isNotBlank() &&
                //subcategoria.text.isEmpty() &&
                precio.text.isNotBlank() &&
                stock.text.isNotBlank() &&
                imagenClave.text.isNotBlank() &&
                descripcion.text.isNotBlank()
                ) {
            return true
        } else {
            return false
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") }
            )
        }
    ) {
        padding -> Column(
            modifier = Modifier
                .padding(padding)
                .padding(14.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        )
        {
            TextButton(onClick = { navController.navigate("admin") }) {
                Text("Volver")
            }

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("Sku") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria") },
                modifier = Modifier.fillMaxWidth()
            )

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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                            .background(Color.White)
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
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imagenClave,
                onValueChange = { imagenClave = it },
                label = { Text("Imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            val scope = rememberCoroutineScope()

            TextButton(onClick = {
                if (producto_agregado() == true) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Producto agregado")

                        kotlinx.coroutines.delay(1000)

                        navController.navigate("admin")
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("ERROR: Campos incompletos")
                    }
                }
            }) {
                Text("Agregar producto")
            }
        }
    }
}