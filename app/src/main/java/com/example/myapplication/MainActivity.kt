package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.screens.PostScreen
import com.example.myapplication.viewmodel.PostViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la app dibuje contenido debajo de las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, decorFitsSystemWindows = false)

        setContent {
            MyApplicationTheme {
                // Inyectamos el ViewModel
                val postViewModel: PostViewModel = viewModel()

                // Mostramos la pantalla de posts
                PostScreen(viewModel = postViewModel)
            }
        }
    }
}