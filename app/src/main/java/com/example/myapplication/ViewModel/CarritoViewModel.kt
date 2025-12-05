package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Carrito
import com.example.myapplication.Model.Producto
import com.example.myapplication.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarritoViewModel : ViewModel() {

    private val _carrito = MutableStateFlow<List<Carrito>>(emptyList())
    val carrito: StateFlow<List<Carrito>> = _carrito

    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total

    private fun recalcularTotal() {
        _total.value = _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    fun agregarAlCarrito(producto: Producto) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val item = listaActual[index]
            listaActual[index] = item.copy(cantidad = item.cantidad + 1)
        } else {
            listaActual.add(Carrito(producto, 1))
        }

        _carrito.value = listaActual
        recalcularTotal()
    }

    fun eliminarDelCarrito(productoId: Int) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == productoId }
        recalcularTotal()
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
        recalcularTotal()
    }

    fun actualizarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarDelCarrito(productoId)
            return
        }

        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == productoId }

        if (index >= 0) {
            val item = listaActual[index]
            listaActual[index] = item.copy(cantidad = nuevaCantidad)
        }

        _carrito.value = listaActual
        recalcularTotal()
    }

    fun checkoutRemoto(
        idCliente: Long,
        onResultado: (Boolean, String) -> Unit
    ) {
        val itemsActuales = _carrito.value
        if (itemsActuales.isEmpty()) {
            onResultado(false, "El carrito está vacío")
            return
        }

        viewModelScope.launch {
            try {
                val api = RetrofitClient.apiService

                try {
                    api.vaciarCarritoRemoto(idCliente)
                } catch (_: Exception) { }

                for (item in itemsActuales) {
                    api.agregarProductoAlCarrito(
                        idCliente = idCliente,
                        productoId = item.producto.id.toLong(),
                        cantidad = item.cantidad
                    )
                }

                val venta = api.checkout(idCliente)

                vaciarCarrito()

                onResultado(true, "Compra realizada. Total: ${venta.total} CLP")

            } catch (e: Exception) {
                e.printStackTrace()
                onResultado(false, "Error: ${e.message}")
            }
        }
    }
}
