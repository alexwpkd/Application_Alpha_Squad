package com.example.myapplication.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Data.Comuna
import com.example.myapplication.Data.Region
import com.example.myapplication.Data.comunas
import com.example.myapplication.Data.regiones
import com.example.myapplication.R
import com.example.myapplication.ViewModel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var regionSeleccionada by remember { mutableStateOf<Region?>(null) }
    var comunaSeleccionada by remember { mutableStateOf<Comuna?>(null) }

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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de Alpha Squad",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("~ Alpha Squad ~", style = MaterialTheme.typography.titleLarge, color = Color(0xFFEDEDED))

            Spacer(modifier = Modifier.height(40.dp))

            Text("Registro", style = MaterialTheme.typography.headlineSmall, color = Color(0xFFEDEDED))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                label = "Región",
                items = regiones.map { it.nombre },
                selectedItem = regionSeleccionada?.nombre ?: "",
                onItemSelected = {
                    regionSeleccionada = regiones.find { r -> r.nombre == it }
                    comunaSeleccionada = null
                }
            )

            val comunasFiltradas = comunas.filter {
                it.regionId == regionSeleccionada?.id
            }

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                label = "Comuna",
                items = comunasFiltradas.map { it.nombre },
                selectedItem = comunaSeleccionada?.nombre ?: "",
                enabled = regionSeleccionada != null,
                onItemSelected = {
                    comunaSeleccionada = comunasFiltradas.find { c -> c.nombre == it }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.registrar(
                        nombre = nombre,
                        apellidos = apellidos,
                        rut = rut,
                        regionSeleccionada = regionSeleccionada != null,
                        comunaSeleccionada = comunaSeleccionada != null,
                        direccion = direccion,
                        email = email,
                        password = password
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6650A4),
                    contentColor = Color(0xFFEDEDED)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            if (viewModel.mensaje.value.isNotEmpty()) {
                Text(
                    text = viewModel.mensaje.value,
                    modifier = Modifier.padding(top = 10.dp),
                    color = if (viewModel.mensaje.value.contains("Error"))
                        MaterialTheme.colorScheme.error
                    else
                        Color(0xFFEDEDED)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFFEDEDED))
            }
        }
    }
}
