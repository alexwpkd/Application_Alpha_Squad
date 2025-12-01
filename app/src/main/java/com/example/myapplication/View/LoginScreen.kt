package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ViewModel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loading by viewModel.loading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val loginResponse by viewModel.loginResponse.collectAsState()

    // Cuando el login sea exitoso, navegamos según el rol
    LaunchedEffect(loginResponse) {
        loginResponse?.let { resp ->
            when (resp.rol) {
                "ADMIN" -> {
                    navController.navigate("admin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "CLIENTE" -> {
                    // Tu ruta actual es "home/{email}", así que pasamos el correo real
                    navController.navigate("home/${resp.correo}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "EMPLEADO" -> {
                    // Por ahora lo mandamos también a admin, puedes cambiarlo a otra pantalla
                    navController.navigate("admin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                else -> {
                    // Si llega un rol raro, al menos lo mandamos a home genérico
                    navController.navigate("home/${resp.correo}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }

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

        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ingresando...")
            } else {
                Text("Entrar")
            }
        }

        if (mensaje.isNotBlank()) {
            Text(
                mensaje,
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error.takeIf { mensaje.contains("Error", true) || mensaje.contains("incorrectas", true) }
                    ?: MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? ¡Regístrate!")
        }

        // IMPORTANTE: aquí antes tenías "home/{email}" literal.
        // Ahora pasamos el valor de 'email'.
        TextButton(onClick = {
            if (email.isNotBlank()) {
                navController.navigate("home/$email")
            }
        }) {
            Text("Entrar a home (debug)")
        }
    }
}