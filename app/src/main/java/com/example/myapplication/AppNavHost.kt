package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.View.LoginScreen
import com.example.myapplication.View.RegisterScreen
import com.example.myapplication.View.HomeScreen
import com.example.myapplication.View.CarritoScreen
import com.example.myapplication.View.Administrador
import com.example.myapplication.View.administrador
import com.example.myapplication.ViewModel.AuthViewModel
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.ui2.CatalogoScreen
import com.example.myapplication.ui2.DetalleProductoScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // LOGIN
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        // REGISTER
        composable("register") {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        // HOME with email param
        composable(
            route = "home/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            // If you have a HomeScreen, use it; otherwise show catalog
            HomeScreen(navController = navController, email = email)
        }

        // CATÁLOGO (two spellings to match your existing usages)
        composable("catalogo") {
            CatalogoScreen(navController = navController, catalogViewModel = catalogoViewModel, carritoViewModel = carritoViewModel)
        }
        composable("catalogue") {
            CatalogoScreen(navController = navController, catalogViewModel = catalogoViewModel, carritoViewModel = carritoViewModel)
        }

        // DETALLE
        composable(
            route = "detalle/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(productoId = id, viewModel = catalogoViewModel, carritoViewModel = carritoViewModel, navController = navController)
        }

        // CARRITO
        composable("carrito") {
            CarritoScreen(navController = navController, carritoViewModel = carritoViewModel)
        }

        // ADMIN
        composable("admin") {
            administrador(navController)
        }
    }
}
