package com.example.myapplication.ViewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.example.myapplication.Model.FakeDatabase
import com.example.myapplication.Model.Usuario
import org.json.JSONObject
import java.nio.charset.Charset
import android.util.Patterns //Validar email
import java.io.File
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var mensaje = mutableStateOf("")
    private val gson by lazy { GsonBuilder().setPrettyPrinting().create() }

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
    /** Crea (si no existe) una carpeta interna 'usuarios' dentro de filesDir */
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

    /** Carga todos los usuarios guardados como lista mutable */
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


    fun registrar(
        nombre: String,
        //apellido: String,
        rut: String,
        //region: String,
        //comuna: String,
        direccion: String,
        email: String,
        password: String,
        //telefono: String
    )

    {
        val nombreTrim = nombre.trim()
        val rutTrim = rut.trim()
        val direccionTrim = direccion.trim()
        val emailTrim = email.trim()
        val passwordRaw = password

        if (!noVacio(nombreTrim)) {
            mensaje.value = "El nombre es obligatorio"
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

        val usuario_registro = Usuario(
            nombreTrim,
            //apellido,
            rutFormateado, // guardamos el RUT formateado
            //region,
            //comuna,
            direccionTrim,
            emailTrim,
            passwordRaw
            //telefono
        )

        if (FakeDatabase.registrar(usuario_registro)) {
            agregarUsuarioAlJson(usuario_registro)
            mensaje.value = "Registro exitoso"
        } else {
            mensaje.value = "El usuario ya existe"
        }
    }

    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    var usuarioActual = mutableStateOf<String?>(null)

    fun login(
        email: String,
        password: String,
        navController: NavController
    ): Boolean {

        if (esAdmin(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Acceso consedido a usuario administrador"
            return true
        }

        return if (FakeDatabase.login(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Sesion iniciada"
            navController.navigate("home/$email")
            false
        } else {
            mensaje.value = "Credenciales inválidas!"
            false
        }
    }

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
