package com.example.myapplication.ViewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.example.myapplication.Model.FakeDatabase
import com.example.myapplication.Model.Usuario
import org.json.JSONObject
import java.nio.charset.Charset

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    var mensaje = mutableStateOf("")

    fun registrar(
        nombre: String,
        apellido: String,
        rut: String,
        region: String,
        comuna: String,
        direccion: String,
        email: String,
        password: String,
        telefono: String
    )

    {
        val usuario_registro = Usuario(
            nombre,
            apellido,
            rut,
            region,
            comuna,
            direccion,
            email,
            password,
            telefono
        )

        if (FakeDatabase.registrar(usuario_registro)) {
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
        //alex, si ves esto, me dio flojera hacer esto con otro json util, con una lista
        //y todo el tema, a si que espero que con un puro admin baste, por eso use JSONObject
    }
}
