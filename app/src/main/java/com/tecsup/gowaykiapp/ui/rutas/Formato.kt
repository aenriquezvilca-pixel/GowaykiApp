package com.tecsup.gowaykiapp.ui.rutas

import androidx.compose.ui.graphics.Color
import com.tecsup.gowaykiapp.ui.theme.GwAmber
import com.tecsup.gowaykiapp.ui.theme.GwGreen
import com.tecsup.gowaykiapp.ui.theme.GwRed

val ESTADOS = listOf("en_revision", "aprobada", "requiere_cambios")
val TIPOS = listOf("peatonal", "ciclista", "transporte")

fun estadoLabel(estado: String): String = when (estado) {
    "en_revision" -> "En revisión"
    "aprobada" -> "Aprobada"
    "requiere_cambios" -> "Requiere cambios"
    else -> estado
}

fun estadoColor(estado: String): Color = when (estado) {
    "aprobada" -> GwGreen
    "requiere_cambios" -> GwRed
    else -> GwAmber
}

fun tipoLabel(tipo: String): String = when (tipo) {
    "peatonal" -> "Peatonal"
    "ciclista" -> "Ciclista"
    "transporte" -> "Transporte"
    else -> tipo
}

private val MESES = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")

/** "2026-10-10" -> "10 oct 2026" */
fun fechaCorta(fecha: String): String {
    val partes = fecha.split("-")
    if (partes.size != 3) return fecha
    val mes = partes[1].toIntOrNull()?.let { MESES.getOrNull(it - 1) } ?: partes[1]
    return "${partes[2].toIntOrNull() ?: partes[2]} $mes ${partes[0]}"
}

fun formatoKm(km: Double): String = String.format(java.util.Locale.US, "%.1f", km)
