package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CompraTacticaViewModel : ViewModel() {

    // productoId -> cantidad seleccionada
    private val _seleccion = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val seleccion: StateFlow<Map<Int, Int>> = _seleccion

    /**
     * @return true si incrementó, false si ya estaba en el límite (stockMax) o stockMax inválido.
     */
    fun incrementar(productoId: Int, stockMax: Int): Boolean {
        if (stockMax <= 0) return false

        val actual = _seleccion.value[productoId] ?: 0
        if (actual >= stockMax) return false  // 👈 ya está al máximo, avisaremos desde UI

        val nuevo = actual + 1
        _seleccion.value = _seleccion.value.toMutableMap().apply {
            put(productoId, nuevo)
        }
        return true
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
