package com.example.myapplication
/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel // <--- IMPORTAR ViewModel
import com.example.myapplication.ViewModel.AuthViewModel
import com.example.myapplication.View.RegisterScreen // <--- IMPORTAR pantallas
import com.example.myapplication.View.LoginScreen
import com.example.myapplication.View.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()

            NavHost(navController, startDestination = "register") {
                composable("register") {
                    RegisterScreen(navController, viewModel)
                }
                composable("login") {
                    LoginScreen(navController, viewModel)
                }
                composable("home/{email}") { backStack ->
                    val email = backStack.arguments?.getString("email")
                    HomeScreen(email)
                }
            }
        }
    }
}
*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.CatalogoScreen
import com.example.myapplication.ui.DetalleProductoScreen
import com.example.myapplication.ViewModel.CatalogoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = remember { CatalogoViewModel() }

            NavHost(navController = navController, startDestination = "catalogo") {
                composable("catalogo") {
                    CatalogoScreen(navController = navController, viewModel = viewModel)
                }
                composable("detalle/{productoId}") { backStack ->
                    val idStr = backStack.arguments?.getString("productoId")
                    val id = idStr?.toIntOrNull() ?: -1
                    DetalleProductoScreen(productoId = id, viewModel = viewModel)
                }
            }
        }
    }
}