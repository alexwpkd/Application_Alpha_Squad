package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Carrito
import com.example.myapplication.Model.Producto
import com.example.myapplication.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarrionViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<Carrito>>(emptyList())
    val items: StateFlow<List<Carrito>> = _items



    fun agregarAlCarrito(producto: Producto) {
        val listaActual = _items.value.toMutableList()
        val existenteIndex = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (existenteIndex >= 0) {
            val existente = listaActual[existenteIndex]
            listaActual[existenteIndex] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            listaActual.add(Carrito(producto = producto, cantidad = 1))
        }

        _items.value = listaActual
    }

    fun eliminarDelCarrito(productoId: Int) {
        val nuevaLista = _items.value.filterNot { it.producto.id == productoId }
        _items.value = nuevaLista
    }

    fun vaciarCarrito() {
        _items.value = emptyList()
    }

    fun total(): Int =
        _items.value.sumOf { it.producto.precio * it.cantidad }


    fun checkoutRemoto(
        idCliente: Long,
        onResultado: (Boolean, String) -> Unit
    ) {
        val itemsActuales = _items.value
        if (itemsActuales.isEmpty()) {
            onResultado(false, "El carrito está vacío")
            return
        }

        viewModelScope.launch {
            try {
                val api = RetrofitClient.apiService


                try {
                    api.vaciarCarritoRemoto(idCliente)
                } catch (_: Exception) {

                }


                for (item in itemsActuales) {
                    api.agregarProductoAlCarrito(
                        idCliente = idCliente,
                        productoId = item.producto.id.toLong(),
                        cantidad = item.cantidad
                    )
                }


                val venta = api.checkout(idCliente)
                _items.value = emptyList()

                onResultado(true, "Compra realizada. Total: ${venta.total} CLP")

            } catch (e: Exception) {
                e.printStackTrace()
                onResultado(false, "Error al procesar la compra: ${e.message}")
            }
        }
    }
}
