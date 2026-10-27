package com.tecsup.gowaykiapp.data.repository

import com.tecsup.gowaykiapp.data.model.Ruta
import com.tecsup.gowaykiapp.data.remote.RetrofitClient
import com.tecsup.gowaykiapp.data.remote.RutaApi
import retrofit2.HttpException

class RutaRepository(private val api: RutaApi = RetrofitClient.rutaApi) {

    suspend fun listar(estado: String? = null): Result<List<Ruta>> =
        runCatching { api.listar(estado) }.mapError()

    suspend fun crear(ruta: Ruta): Result<Ruta> =
        runCatching { api.crear(ruta) }.mapError()

    suspend fun modificar(id: Int, ruta: Ruta): Result<Ruta> =
        runCatching { api.modificar(id, ruta) }.mapError()

    suspend fun eliminar(id: Int): Result<Unit> =
        runCatching {
            val respuesta = api.eliminar(id)
            if (!respuesta.isSuccessful) throw HttpException(respuesta)
        }.mapError()

    private fun <T> Result<T>.mapError(): Result<T> = recoverCatching { e ->
        throw Exception(mensajeError(e))
    }

    private fun mensajeError(e: Throwable): String = when (e) {
        is HttpException -> {
            val cuerpo = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
            "Error ${e.code()}" + if (!cuerpo.isNullOrBlank()) ": $cuerpo" else ""
        }
        else -> "No se pudo conectar con el servidor. Revisa que la API esté corriendo."
    }
}
