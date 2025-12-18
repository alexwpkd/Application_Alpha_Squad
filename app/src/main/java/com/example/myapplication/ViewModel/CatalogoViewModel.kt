package com.example.myapplication.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Producto
import com.example.myapplication.remote.RetrofitClient
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
                _productos.value = repo.getProductos(context)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarProducto(id: Long, context: Context) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.eliminarProducto(id)

                _productos.value = repo.getProductos(context)

            } catch (e: Exception) {
                _error.value = e.message ?: "Error al eliminar"
            } finally {
                _loading.value = false
            }
        }
    }

    fun buscarProductoPorId(id: Int): Producto? =
        _productos.value.find { it.id == id }
}
