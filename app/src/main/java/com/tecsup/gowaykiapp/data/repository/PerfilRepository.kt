package com.tecsup.gowaykiapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.gowaykiapp.ui.perfil.PerfilUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.perfilDataStore by preferencesDataStore(name = "perfil_prefs")

/** Guarda el perfil y las preferencias (tema, notificaciones) con DataStore. */
class PerfilRepository(private val context: Context) {

    private object Claves {
        val nombre = stringPreferencesKey("nombre")
        val correo = stringPreferencesKey("correo")
        val ciudad = stringPreferencesKey("ciudad")
        val telefono = stringPreferencesKey("telefono")
        val descripcion = stringPreferencesKey("descripcion")
        val tema = stringPreferencesKey("tema") // "claro" | "oscuro" | ausente = sistema
        val notificaciones = booleanPreferencesKey("notificaciones")
        val miembroDesde = stringPreferencesKey("miembro_desde") // "yyyy-MM"
    }

    val perfil: Flow<PerfilUiState> = context.perfilDataStore.data.map { prefs ->
        val base = PerfilUiState()
        PerfilUiState(
            nombre = prefs[Claves.nombre] ?: base.nombre,
            correo = prefs[Claves.correo] ?: base.correo,
            ciudad = prefs[Claves.ciudad] ?: base.ciudad,
            telefono = prefs[Claves.telefono] ?: base.telefono,
            descripcion = prefs[Claves.descripcion] ?: base.descripcion,
            temaOscuro = when (prefs[Claves.tema]) {
                "oscuro" -> true
                "claro" -> false
                else -> null
            },
            notificaciones = prefs[Claves.notificaciones] ?: base.notificaciones,
            miembroDesde = prefs[Claves.miembroDesde],
            cargado = true
        )
    }

    suspend fun guardarDatos(
        nombre: String,
        correo: String,
        ciudad: String,
        telefono: String,
        descripcion: String
    ) {
        context.perfilDataStore.edit { prefs ->
            prefs[Claves.nombre] = nombre
            prefs[Claves.correo] = correo
            prefs[Claves.ciudad] = ciudad
            prefs[Claves.telefono] = telefono
            prefs[Claves.descripcion] = descripcion
        }
    }

    suspend fun guardarTema(oscuro: Boolean) {
        context.perfilDataStore.edit { it[Claves.tema] = if (oscuro) "oscuro" else "claro" }
    }

    suspend fun guardarNotificaciones(activadas: Boolean) {
        context.perfilDataStore.edit { it[Claves.notificaciones] = activadas }
    }

    suspend fun reiniciar() {
        context.perfilDataStore.edit {
            it.clear()
            it[Claves.miembroDesde] = mesActual()
        }
    }

    /** Guarda el mes en que se abrió la app por primera vez, si todavía no existe. */
    suspend fun asegurarFechaRegistro() {
        context.perfilDataStore.edit { prefs ->
            if (prefs[Claves.miembroDesde] == null) prefs[Claves.miembroDesde] = mesActual()
        }
    }

    private fun mesActual(): String =
        SimpleDateFormat("yyyy-MM", Locale.US).format(Date())
}
