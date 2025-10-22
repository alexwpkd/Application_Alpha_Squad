package com.example.myapplication

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