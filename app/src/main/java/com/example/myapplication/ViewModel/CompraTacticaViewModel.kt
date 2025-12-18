package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CompraTacticaViewModel : ViewModel() {

    // productoId -> cantidad seleccionada
    private val _seleccion = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val seleccion: StateFlow<Map<Int, Int>> = _seleccion

    fun incrementar(productoId: Int, stockMax: Int) {
        val actual = _seleccion.value[productoId] ?: 0
        val nuevo = (actual + 1).coerceAtMost(stockMax)
        _seleccion.value = _seleccion.value.toMutableMap().apply {
            if (nuevo <= 0) remove(productoId) else put(productoId, nuevo)
        }
    }

    fun decrementar(productoId: Int) {
        val actual = _seleccion.value[productoId] ?: 0
        val nuevo = actual - 1
        _seleccion.value = _seleccion.value.toMutableMap().apply {
            if (nuevo <= 0) remove(productoId) else put(productoId, nuevo)
        }
    }

    fun limpiar() {
        _seleccion.value = emptyMap()
    }
}
