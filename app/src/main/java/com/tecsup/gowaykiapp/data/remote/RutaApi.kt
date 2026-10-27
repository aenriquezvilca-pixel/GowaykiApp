package com.tecsup.gowaykiapp.data.remote

import com.tecsup.gowaykiapp.data.model.Ruta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RutaApi {

    @GET("api/rutas/")
    suspend fun listar(@Query("estado") estado: String? = null): List<Ruta>

    @GET("api/rutas/{id}/")
    suspend fun ver(@Path("id") id: Int): Ruta

    @POST("api/rutas/")
    suspend fun crear(@Body ruta: Ruta): Ruta

    @PUT("api/rutas/{id}/")
    suspend fun modificar(@Path("id") id: Int, @Body ruta: Ruta): Ruta

    @DELETE("api/rutas/{id}/")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}
