package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ViewModel.AuthViewModel
import com.example.myapplication.View.*
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ui2.CatalogoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()
            val catalogViewModel : CatalogoViewModel = CatalogoViewModel()

            NavHost(navController, startDestination = "register") {
                composable("register") {
                    RegisterScreen(navController, viewModel)
                }

                composable("login") {
                    LoginScreen(navController, viewModel)
                }

                composable("home/{email}") { backStack ->
                    val email = backStack.arguments?.getString("email")
                    HomeScreen(navController, email)
                }

                composable("cart") {
                    CarritoScreen(navController)
                }

                composable("catalogue") {
                    CatalogoScreen(navController, catalogViewModel)
                }
            }
        }
    }
}

/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui2.CatalogoScreen
import com.example.myapplication.ui2.DetalleProductoScreen
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
                    DetalleProductoScreen(productoId = id, ViewModel = viewModel)
                }
            }
        }
    }
}
 */