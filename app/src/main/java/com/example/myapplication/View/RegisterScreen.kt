package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ViewModel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    //var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    //var region by remember { mutableStateOf("") }
    //var comuna by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    //var telefono by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "~ Alpha Squad ~",
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Spacer(modifier = Modifier.height(40.dp))

        Text("Registro", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        //OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
        OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("Rut") })
        //OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Region") })
        //OutlinedTextField(value = comuna, onValueChange = { comuna = it }, label = { Text("Comuna") })
        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Direccion") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") })
        //OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Telefono") })

        Spacer(modifier = Modifier.height(10.dp))

        //Button(onClick = { viewModel.registrar(nombre, apellido, rut, region, comuna, direccion, email, password, telefono) }) {
        Button(onClick = { viewModel.registrar(nombre, rut, direccion, email, password) }) {
            Text("Registrar")
        }

        Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 10.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}