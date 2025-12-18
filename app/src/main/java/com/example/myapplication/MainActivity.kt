package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.View.*
import com.example.myapplication.ViewModel.AuthViewModel
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.ViewModel.CompraTacticaViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui2.CarritoScreen
import com.example.myapplication.ui2.CatalogoScreen
import com.example.myapplication.ui2.CompraTacticaScreen
import com.example.myapplication.ui2.DetalleProductoScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val authViewModel: AuthViewModel = viewModel()
                    val catalogoViewModel: CatalogoViewModel = viewModel()
                    val carritoViewModel: CarritoViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "login") {

                        composable("register") {
                            RegisterScreen(navController, authViewModel)
                        }

                        composable("login") {
                            LoginScreen(navController, authViewModel)
                        }

                        // ✅ Home sin email (para volver sin romper ruta)
                        composable("home") {
                            HomeScreen(navController, email = null)
                        }

                        composable("home/{email}") { backStack ->
                            val email = backStack.arguments?.getString("email")
                            HomeScreen(navController, email)
                        }

                        composable("carrito") {
                            CarritoScreen(carritoViewModel, navController)
                        }

                        composable("catalogue") {
                            CatalogoScreen(navController, catalogoViewModel, carritoViewModel)
                        }

                        // ✅ NUEVO: Compra táctica
                        composable("tactica") {
                            val tacticaViewModel: CompraTacticaViewModel = viewModel()
                            CompraTacticaScreen(
                                navController = navController,
                                catalogoViewModel = catalogoViewModel,
                                carritoViewModel = carritoViewModel,
                                tacticaViewModel = tacticaViewModel
                            )
                        }

                        composable("admin") {
                            administrador(navController)
                        }

                        composable("adminCatalogue") {
                            AdminCatalogScreen(navController, catalogoViewModel)
                        }

                        composable("adminProductAdd") {
                            administradorAgregarProducto(navController)
                        }

                        composable(
                            route = "detalle/{productoId}",
                            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("productoId") ?: -1
                            DetalleProductoScreen(
                                productoId = id,
                                viewModel = catalogoViewModel,
                                carritoViewModel = carritoViewModel,
                                navController = navController
                            )
                        }

                        composable(
                            route = "admin/editar/{productoId}",
                            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val productoId = backStackEntry.arguments?.getLong("productoId") ?: 0L
                            administradorEditarProducto(
                                navController = navController,
                                catalogViewModel = catalogoViewModel,
                                productoId = productoId
                            )
                        }

                        composable(
                            route = "admin/detalle/{productoId}",
                            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val productoId = backStackEntry.arguments?.getLong("productoId") ?: 0L
                            AdminDetalleProductoScreen(
                                navController = navController,
                                catalogViewModel = catalogoViewModel,
                                productoId = productoId
                            )
                        }
                    }
                }
            }
        }
    }
}
