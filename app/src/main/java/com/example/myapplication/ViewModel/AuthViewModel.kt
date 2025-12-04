package com.example.myapplication.ViewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.Model.FakeDatabase
import com.example.myapplication.Model.Usuario
import com.example.myapplication.remote.ClienteRegistroRequest
import com.example.myapplication.remote.LoginRequest
import com.example.myapplication.remote.RetrofitClient
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var mensaje = mutableStateOf("")
    private val gson by lazy { GsonBuilder().setPrettyPrinting().create() }

    // ================== VALIDACIONES ==================

    private fun validarRut(rut: String): Boolean {
        val clean = rut.replace(".", "").replace("-", "").replace(" ", "").uppercase()
        if (clean.length < 2) return false

        val dvIngresado = clean.last().toString()
        val cuerpo = clean.dropLast(1)

        if (cuerpo.isEmpty() || !cuerpo.all { it.isDigit() }) return false
        if (!dvIngresado.all { it.isDigit() || it == 'K' }) return false

        var suma = 0
        var multiplicador = 2
        for (c in cuerpo.reversed()) {
            suma += (c - '0') * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }
        val resto = suma % 11
        val dvCalculado = when (val res = 11 - resto) {
            11 -> "0"
            10 -> "K"
            else -> res.toString()
        }
        return dvIngresado == dvCalculado
    }

    private fun formatearRut(rut: String): String {
        val clean = rut.replace(".", "").replace("-", "").replace(" ", "").uppercase()
        if (clean.length < 2) return rut
        val dv = clean.last()
        val cuerpo = clean.dropLast(1)

        val sb = StringBuilder()
        var count = 0
        for (i in cuerpo.length - 1 downTo 0) {
            sb.append(cuerpo[i])
            count++
            if (count == 3 && i != 0) {
                sb.append('.')
                count = 0
            }
        }
        return sb.reverse().toString() + "-" + dv
    }

    private fun validarEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return regex.matches(email)
    }

    private fun validarPassword(password: String): Boolean =
        password.length >= 4 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }

    private fun passwordError(password: String): String? {
        val faltas = mutableListOf<String>()
        if (password.length < 4) faltas += "mín. 4 caracteres"
        if (!password.any { it.isUpperCase() }) faltas += "1 mayúscula"
        if (!password.any { it.isLowerCase() }) faltas += "1 minúscula"
        return if (faltas.isEmpty()) null else "La contraseña debe tener: ${faltas.joinToString(", ")}."
    }

    private fun noVacio(texto: String): Boolean = texto.trim().isNotEmpty()

    // ================== ARCHIVO JSON LOCAL DE USUARIOS (opcional, lo dejo por si lo usas) ==================

    private fun usuariosDir(): File {
        val dir = File(getApplication<Application>().filesDir, "usuarios")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun usuariosFile(): File {
        val file = File(usuariosDir(), "usuario.json")
        if (!file.exists()) file.writeText("[]")
        return file
    }

    private fun cargarUsuariosDesdeArchivo(): MutableList<Usuario> {
        val file = usuariosFile()
        return try {
            val json = file.readText()
            val type = object : TypeToken<MutableList<Usuario>>() {}.type
            gson.fromJson<MutableList<Usuario>>(json, type) ?: mutableListOf()
        } catch (_: Exception) {
            mutableListOf()
        }
    }

    private fun guardarUsuariosAArchivo(usuarios: List<Usuario>) {
        val file = usuariosFile()
        file.writeText(gson.toJson(usuarios))
    }

    private fun agregarUsuarioAlJson(usuario: Usuario) {
        val lista = cargarUsuariosDesdeArchivo()
        val idx = lista.indexOfFirst {
            it.email.equals(usuario.email, ignoreCase = true) || it.rut == usuario.rut
        }
        if (idx >= 0) {
            lista[idx] = usuario
        } else {
            lista.add(usuario)
        }
        guardarUsuariosAArchivo(lista)
    }

    // ================== REGISTRO (AHORA USA BACKEND) ==================

    fun registrar(
        nombre: String,
        apellidos: String,
        rut: String,
        direccion: String,
        email: String,
        password: String,
    ) {
        val nombreTrim = nombre.trim()
        val apellidosTrim = apellidos.trim()
        val rutTrim = rut.trim()
        val direccionTrim = direccion.trim()
        val emailTrim = email.trim()
        val passwordRaw = password

        if (!noVacio(nombreTrim)) {
            mensaje.value = "El nombre es obligatorio"
            return
        }

        if (!noVacio(apellidosTrim)) {
            mensaje.value = "El apellido es obligatorio"
            return
        }

        if (!validarRut(rutTrim)) {
            mensaje.value = "RUT inválido. Ejemplo: 12.345.678-5"
            return
        }
        val rutFormateado = formatearRut(rutTrim)

        if (!noVacio(direccionTrim)) {
            mensaje.value = "La dirección es obligatoria"
            return
        }

        if (!validarEmail(emailTrim)) {
            mensaje.value = "Email no válido"
            return
        }

        if (!validarPassword(passwordRaw)) {
            mensaje.value = passwordError(passwordRaw) ?: "La contraseña no cumple los requisitos"
            return
        }

        viewModelScope.launch {
            try {
                mensaje.value = ""

                val req = ClienteRegistroRequest(
                    nombre = nombreTrim,
                    apellidos = apellidosTrim,
                    rut = rutFormateado,
                    correo = emailTrim,
                    password = passwordRaw,
                    direccion = direccionTrim
                )

                val resp = RetrofitClient.apiService.registrarCliente(req)

                // opcional: también lo guardas localmente si quieres
                val usuarioRegistro = Usuario(
                    nombreTrim,
                    rutFormateado,
                    direccionTrim,
                    emailTrim,
                    passwordRaw
                )
                agregarUsuarioAlJson(usuarioRegistro)

                mensaje.value = "Registro exitoso para ${resp.correo}"

            } catch (e: Exception) {
                e.printStackTrace()
                mensaje.value = "Error al registrar: ${e.message}"
            }
        }
    }

    // ================== LOGIN (YA USA BACKEND) ==================

    var usuarioActual = mutableStateOf<String?>(null)

    fun login(
        email: String,
        password: String,
        navController: NavController
    ): Boolean {
        val emailTrim = email.trim()
        val passwordTrim = password.trim()

        viewModelScope.launch {
            if (!validarEmail(emailTrim)) {
                mensaje.value = "Email no válido"
                return@launch
            }
            if (!validarPassword(passwordTrim)) {
                mensaje.value = passwordError(passwordTrim) ?: "La contraseña no cumple los requisitos"
                return@launch
            }

            try {
                mensaje.value = ""

                val resp = RetrofitClient.apiService.login(
                    LoginRequest(
                        correo = emailTrim,
                        password = passwordTrim
                    )
                )

                RetrofitClient.setToken(resp.token)

                usuarioActual.value = resp.correo

                mensaje.value = when (resp.rol) {
                    "ADMIN" -> "Acceso concedido a usuario administrador"
                    "EMPLEADO" -> "Bienvenido empleado"
                    "CLIENTE" -> "Sesión iniciada"
                    else -> "Sesión iniciada"
                }

                when (resp.rol) {
                    "ADMIN" -> navController.navigate("admin")
                    else -> navController.navigate("home/${resp.correo}")
                }

            } catch (e: Exception) {
                e.printStackTrace()

                if (esAdmin(emailTrim, passwordTrim)) {
                    usuarioActual.value = emailTrim
                    mensaje.value = "Acceso concedido a usuario administrador (local)"
                    navController.navigate("admin")
                } else if (FakeDatabase.login(emailTrim, passwordTrim)) {
                    usuarioActual.value = emailTrim
                    mensaje.value = "Sesión iniciada (modo local)"
                    navController.navigate("home/$emailTrim")
                } else {
                    mensaje.value = "Credenciales inválidas o error de conexión"
                }
            }
        }

        return false
    }

    // ================== ADMIN LOCAL (JSON EN assets) ==================

    private fun esAdmin(
        email: String,
        password: String
    ): Boolean {
        val context = getApplication<Application>().applicationContext

        val jsonString = context.assets.open("admin.json").use {
            it.readBytes().toString(Charset.defaultCharset())
        }

        val adminData = JSONObject(jsonString).getJSONObject("admin")
        val adminEmail = adminData.getString("email")
        val adminPassword = adminData.getString("password")

        return email == adminEmail && password == adminPassword
    }
}
