package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ViewModel.AuthViewModel
import com.example.myapplication.ViewModel.CatalogoViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                // ViewModels compartidos
                val authViewModel: AuthViewModel = viewModel()
                val catalogoViewModel: CatalogoViewModel = viewModel()
                val carritoViewModel: CarritoViewModel = viewModel()

                AppNavHost(
                    navController = navController,
                    authViewModel = authViewModel,
                    catalogoViewModel = catalogoViewModel,
                    carritoViewModel = carritoViewModel
                )
            }
        }
    }
}