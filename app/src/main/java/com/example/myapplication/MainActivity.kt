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
            val catalogViewModel = CatalogoViewModel()

            NavHost(navController, startDestination = "admin") {

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

                composable("admin") {
                    administrador(navController)
                }

                composable("adminCatalogue") {
                    administradorCatalogo(navController)
                }

                composable("adminProductAdd") {
                    administradorAgregarProducto(navController)
                }
            }
        }
    }
}