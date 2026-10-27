package com.tecsup.gowaykiapp.ui.perfil

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PerfilUiState(
    val nombre: String = "Mateo Quispe",
    val correo: String = "mateo.quispe@ejemplo.test",
    val ciudad: String = "Cusco, Perú",
    val telefono: String = "",
    val descripcion: String = "Camino por Cusco y mapeo rutas para una ciudad más accesible.",
    val temaOscuro: Boolean? = null, // null = seguir el tema del sistema
    val notificaciones: Boolean = true
) {
    val iniciales: String
        get() = nombre.trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "?" }
}

class PerfilViewModel : ViewModel() {

    private val _state = MutableStateFlow(PerfilUiState())
    val state: StateFlow<PerfilUiState> = _state.asStateFlow()

    fun guardarDatos(nombre: String, correo: String, ciudad: String, telefono: String, descripcion: String) {
        _state.update {
            it.copy(
                nombre = nombre,
                correo = correo,
                ciudad = ciudad,
                telefono = telefono,
                descripcion = descripcion
            )
        }
    }

    fun cambiarTema(oscuro: Boolean) {
        _state.update { it.copy(temaOscuro = oscuro) }
    }

    fun cambiarNotificaciones(activadas: Boolean) {
        _state.update { it.copy(notificaciones = activadas) }
    }

    /** Vuelve a los datos de ejemplo (se usa al eliminar la cuenta). */
    fun reiniciar() {
        _state.value = PerfilUiState()
    }
}
