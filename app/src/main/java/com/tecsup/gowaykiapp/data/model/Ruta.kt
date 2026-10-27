package com.tecsup.gowaykiapp.data.model

import com.google.gson.annotations.SerializedName

/** Ruta enviada por un voluntario. Coincide con el serializer de la API Django. */
data class Ruta(
    val id: Int? = null,
    val origen: String,
    val destino: String,
    @SerializedName("distancia_km") val distanciaKm: Double,
    val fecha: String, // formato yyyy-MM-dd
    val estado: String = "en_revision", // en_revision | aprobada | requiere_cambios
    val tipo: String = "peatonal", // peatonal | ciclista | transporte
    val descripcion: String = ""
)
