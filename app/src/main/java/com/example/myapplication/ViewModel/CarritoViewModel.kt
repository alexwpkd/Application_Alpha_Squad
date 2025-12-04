package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.Carrito
import com.example.myapplication.Model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarritoViewModel : ViewModel() {

    // Lista de ítems (producto + cantidad)
    private val _items = MutableStateFlow<List<Carrito>>(emptyList())
    val items: StateFlow<List<Carrito>> = _items

    // Total en CLP
    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total

    // Agregar un producto (si ya existe, suma 1 a la cantidad)
    fun agregarAlCarrito(producto: Producto) {
        val listaActual = _items.value.toMutableList()
        val idx = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (idx >= 0) {
            val item = listaActual[idx]
            listaActual[idx] = item.copy(cantidad = item.cantidad + 1)
        } else {
            listaActual.add(Carrito(producto = producto, cantidad = 1))
        }

        _items.value = listaActual
        recalcularTotal()
    }

    fun eliminarDelCarrito(productoId: Int) {
        val nuevaLista = _items.value.filter { it.producto.id != productoId }
        _items.value = nuevaLista
        recalcularTotal()
    }

    fun actualizarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarDelCarrito(productoId)
            return
        }

        val listaActual = _items.value.toMutableList()
        val idx = listaActual.indexOfFirst { it.producto.id == productoId }
        if (idx >= 0) {
            val item = listaActual[idx]
            listaActual[idx] = item.copy(cantidad = nuevaCantidad)
            _items.value = listaActual
            recalcularTotal()
        }
    }

    fun vaciarCarrito() {
        _items.value = emptyList()
        _total.value = 0
    }

    private fun recalcularTotal() {
        val suma = _items.value.sumOf { it.producto.precio * it.cantidad }
        _total.value = suma
    }
}
