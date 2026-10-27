package com.tecsup.gowaykiapp.ui.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.gowaykiapp.data.repository.PerfilRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PerfilUiState(
    val nombre: String = "Mateo Quispe",
    val correo: String = "mateo.quispe@ejemplo.test",
    val ciudad: String = "Cusco, Perú",
    val telefono: String = "",
    val descripcion: String = "Camino por Cusco y mapeo rutas para una ciudad más accesible.",
    val temaOscuro: Boolean? = null, // null = seguir el tema del sistema
    val notificaciones: Boolean = true,
    val miembroDesde: String? = null, // "yyyy-MM", se guarda la primera vez que se abre la app
    val cargado: Boolean = false // true cuando DataStore ya entregó los datos guardados
) {
    val iniciales: String
        get() = nombre.trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "?" }
}

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PerfilRepository(application)

    init {
        viewModelScope.launch { repo.asegurarFechaRegistro() }
    }

    // El estado viene de DataStore, así que sobrevive al cerrar la app
    val state: StateFlow<PerfilUiState> = repo.perfil.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PerfilUiState()
    )

    fun guardarDatos(nombre: String, correo: String, ciudad: String, telefono: String, descripcion: String) {
        viewModelScope.launch { repo.guardarDatos(nombre, correo, ciudad, telefono, descripcion) }
    }

    fun cambiarTema(oscuro: Boolean) {
        viewModelScope.launch { repo.guardarTema(oscuro) }
    }

    fun cambiarNotificaciones(activadas: Boolean) {
        viewModelScope.launch { repo.guardarNotificaciones(activadas) }
    }

    /** Vuelve a los datos de ejemplo (se usa al eliminar la cuenta). */
    fun reiniciar() {
        viewModelScope.launch { repo.reiniciar() }
    }
}
