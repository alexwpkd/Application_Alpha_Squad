package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarritoViewModel : ViewModel() {
    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito = _carrito.asStateFlow()

    fun agregarAlCarrito(producto: Producto) {
        _carrito.value = _carrito.value + producto
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito.value = _carrito.value - producto
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }

    fun realizarCompra(): Boolean {
        return if (_carrito.value.isEmpty()) {
            false
        } else {
            limpiarCarrito()
            true
        }
    }

}
