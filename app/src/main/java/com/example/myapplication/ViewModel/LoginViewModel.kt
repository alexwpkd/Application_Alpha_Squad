package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    fun login(correo: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val resp = repo.login(correo, password)
                _loginResult.value = resp
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al iniciar sesión"
            } finally {
                _loading.value = false
            }
        }
    }
}
