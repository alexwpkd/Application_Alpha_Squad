package com.example.myapplication.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Producto
import com.example.myapplication.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // 👉 AQUÍ ahora va al repo nuevo (API + Room + assets)
                val list = repo.getProductos(context)
                _productos.value = list
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }

    fun buscarProductoPorId(id: Int): Producto? =
        _productos.value.find { it.id == id }
}
