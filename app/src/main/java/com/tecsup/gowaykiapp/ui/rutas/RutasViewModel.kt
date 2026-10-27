package com.tecsup.gowaykiapp.ui.rutas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.gowaykiapp.data.model.Ruta
import com.tecsup.gowaykiapp.data.repository.RutaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RutasUiState(
    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val todas: List<Ruta> = emptyList(), // sin filtro, se usa para estadísticas
    val rutas: List<Ruta> = emptyList(), // las que se muestran según el filtro
    val filtro: String? = null, // null = todas
    val error: String? = null, // error al cargar la lista
    val errorGuardado: String? = null // error al guardar o eliminar (formulario y eliminar cuenta)
)

class RutasViewModel(
    private val repo: RutaRepository = RutaRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RutasUiState())
    val state: StateFlow<RutasUiState> = _state.asStateFlow()

    init {
        cargar()
    }

    fun cambiarFiltro(estado: String?) {
        _state.update { it.copy(filtro = estado) }
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }
            val filtro = _state.value.filtro
            repo.listar(null).fold(
                onSuccess = { todas ->
                    val visibles = if (filtro == null) {
                        todas
                    } else {
                        repo.listar(filtro).getOrElse { todas.filter { r -> r.estado == filtro } }
                    }
                    _state.update { it.copy(cargando = false, todas = todas, rutas = visibles) }
                },
                onFailure = { e ->
                    _state.update { it.copy(cargando = false, error = e.message) }
                }
            )
        }
    }

    fun guardar(ruta: Ruta, onExito: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(guardando = true, errorGuardado = null) }
            val id = ruta.id
            val resultado = if (id == null) repo.crear(ruta) else repo.modificar(id, ruta)
            resultado.fold(
                onSuccess = {
                    _state.update { it.copy(guardando = false) }
                    cargar()
                    onExito()
                },
                onFailure = { e ->
                    _state.update { it.copy(guardando = false, errorGuardado = e.message) }
                }
            )
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            repo.eliminar(id).fold(
                onSuccess = { cargar() },
                onFailure = { e -> _state.update { it.copy(error = e.message) } }
            )
        }
    }

    /** Borra en la API todas las rutas (se usa al eliminar la cuenta). */
    fun eliminarTodas(onListo: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(guardando = true, errorGuardado = null) }
            var fallo: String? = null
            for (ruta in _state.value.todas) {
                val id = ruta.id ?: continue
                repo.eliminar(id).onFailure { e -> fallo = e.message }
            }
            _state.update { it.copy(guardando = false) }
            cargar()
            if (fallo == null) onListo() else _state.update { it.copy(errorGuardado = fallo) }
        }
    }

    fun limpiarError() {
        _state.update { it.copy(error = null, errorGuardado = null) }
    }
}
