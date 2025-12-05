package com.example.myapplication

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.ViewModel.AuthViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.Model.Producto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ExampleUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private fun crearViewModel(): AuthViewModel {
        val app = Mockito.mock(Application::class.java)
        return AuthViewModel(app)
    }

    // ================== TESTS DE REGISTRO ==================

    @Test
    fun registrar_conRutInvalido_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "Juan",
            apellidos = "Pérez",
            rut = "123",
            direccion = "Calle Falsa 123",
            email = "juan@test.com",
            password = "Abc1"
        )

        assertEquals("RUT inválido. Ejemplo: 12.345.678-5", vm.mensaje.value)
    }

    @Test
    fun registrar_conEmailInvalido_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "Juan",
            apellidos = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            email = "correo_invalido",
            password = "Abc1"
        )

        assertEquals("Email no válido", vm.mensaje.value)
    }

    @Test
    fun registrar_conPasswordDebil_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "Juan",
            apellidos = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            email = "test@test.com",
            password = "abc"
        )

        assertTrue(vm.mensaje.value.contains("La contraseña debe"))
    }

    @Test
    fun registrar_conNombreVacio_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "",
            apellidos = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            email = "test@test.com",
            password = "Abc1"
        )

        assertEquals("El nombre es obligatorio", vm.mensaje.value)
    }

    @Test
    fun registrar_conDireccionVacia_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "Juan",
            apellidos = "Pérez",
            rut = "12.345.678-5",
            direccion = "",
            email = "test@test.com",
            password = "Abc1"
        )

        assertEquals("La dirección es obligatoria", vm.mensaje.value)
    }

    @Test
    fun registrar_passwordSinMayuscula_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "Juan",
            apellidos = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            email = "test@test.com",
            password = "abcd"
        )

        assertTrue(vm.mensaje.value.contains("mayúscula"))
    }

    @Test
    fun registrar_passwordSinMinuscula_retornaError() {
        val vm = crearViewModel()

        vm.registrar(
            nombre = "Juan",
            apellidos = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            email = "test@test.com",
            password = "ABCD"
        )

        assertTrue(vm.mensaje.value.contains("minúscula"))
    }

    // ================== TESTS DE CARRITO ==================

    @Test
    fun agregarAlCarrito_productoAgregado() {
        val vm = CarritoViewModel()
        val producto = Producto(
            id = 1,
            sku = "SKU001",
            nombre = "Producto A",
            categoria = "Categoria X",
            subcategoria = "Sub X",
            precio = 1000,
            enStock = true,
            stock = 10,
            imagenClave = 123,
            descripcion = "Producto de prueba"
        )

        vm.agregarAlCarrito(producto)

        val items = vm.items.value
        assertEquals(1, items.size)
        assertEquals(producto, items.first().producto)
        assertEquals(1, items.first().cantidad)
    }

    @Test
    fun eliminarDelCarrito_productoEliminado() {
        val vm = CarritoViewModel()

        val p1 = Producto(
            id = 1,
            sku = "SKU001",
            nombre = "Producto A",
            categoria = "Categoria X",
            subcategoria = "Sub X",
            precio = 1000,
            enStock = true,
            stock = 10,
            imagenClave = 123,
            descripcion = "Producto A descripcion"
        )

        val p2 = Producto(
            id = 2,
            sku = "SKU002",
            nombre = "Producto B",
            categoria = "Categoria X",
            subcategoria = "Sub Y",
            precio = 1500,
            enStock = true,
            stock = 5,
            imagenClave = 456,
            descripcion = "Producto B descripcion"
        )

        vm.agregarAlCarrito(p1)
        vm.agregarAlCarrito(p2)

        // ahora eliminarDelCarrito recibe el ID (Int), no el Producto
        vm.eliminarDelCarrito(p1.id)

        val items = vm.items.value
        assertEquals(1, items.size)
        assertEquals(p2, items.first().producto)
    }

    @Test
    fun realizarCompra_conProductos_vaciaCarrito() {
        val vm = CarritoViewModel()

        val producto = Producto(
            id = 1,
            sku = "SKU001",
            nombre = "Producto A",
            categoria = "Categoria X",
            subcategoria = "Sub X",
            precio = 1000,
            enStock = true,
            stock = 10,
            imagenClave = 123,
            descripcion = "Producto de prueba"
        )

        vm.agregarAlCarrito(producto)

        // antes llamabas a realizarCompra(), ahora usamos vaciarCarrito()
        vm.vaciarCarrito()

        assertTrue(vm.items.value.isEmpty())
    }
}
