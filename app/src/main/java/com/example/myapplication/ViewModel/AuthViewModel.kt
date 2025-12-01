package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse

    fun login(correo: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _mensaje.value = ""
            try {
                val resp = repo.login(correo, password)
                _loginResponse.value = resp

                _mensaje.value = when (resp.rol) {
                    "ADMIN" -> "Administrador autenticado"
                    "CLIENTE" -> "Cliente autenticado"
                    "EMPLEADO" -> "Empleado autenticado"
                    else -> "Usuario autenticado"
                }
            } catch (e: HttpException) {
                _mensaje.value = if (e.code() == 401) {
                    "Credenciales incorrectas"
                } else {
                    "Error del servidor (${e.code()})"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.localizedMessage ?: "desconocido"}"
            } finally {
                _loading.value = false
            }
        }
    }
}
