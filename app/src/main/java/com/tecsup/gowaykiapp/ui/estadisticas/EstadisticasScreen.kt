package com.tecsup.gowaykiapp.ui.estadisticas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.gowaykiapp.data.model.Ruta
import com.tecsup.gowaykiapp.ui.rutas.RutasUiState
import com.tecsup.gowaykiapp.ui.rutas.TIPOS
import com.tecsup.gowaykiapp.ui.rutas.formatoKm
import com.tecsup.gowaykiapp.ui.rutas.tipoLabel

private val MESES_CORTO = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

@Composable
fun EstadisticasScreen(state: RutasUiState, onBack: () -> Unit) {
    val todas = state.todas
    val aprobadas = todas.count { it.estado == "aprobada" }
    val enRevision = todas.count { it.estado == "en_revision" }
    val kmTotal = todas.sumOf { it.distanciaKm }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Estadísticas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        // Tarjeta principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "${todas.size}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("Rutas mapeadas", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "$aprobadas aprobadas · $enRevision en revisión",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DatoCard("Km recorridos", formatoKm(kmTotal), Modifier.weight(1f))
            DatoCard(
                "Requieren cambios",
                "${todas.count { it.estado == "requiere_cambios" }}",
                Modifier.weight(1f)
            )
        }

        RutasPorMes(todas)
        PorTipo(todas)
    }
}

@Composable
private fun DatoCard(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(valor, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(
                titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RutasPorMes(todas: List<Ruta>) {
    val meses = todas
        .filter { it.fecha.length >= 7 }
        .groupBy { it.fecha.take(7) } // "2026-10"
        .toSortedMap()
        .entries
        .toList()
        .takeLast(6)
    val maximo = (meses.maxOfOrNull { it.value.size } ?: 1).coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Rutas por mes", fontWeight = FontWeight.SemiBold)
            if (meses.isEmpty()) {
                Text(
                    "Aún no hay datos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().height(140.dp).padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    meses.forEach { (clave, rutas) ->
                        val etiqueta = clave.substring(5, 7).toIntOrNull()
                            ?.let { MESES_CORTO.getOrNull(it - 1) } ?: clave
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${rutas.size}", style = MaterialTheme.typography.labelSmall)
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .height((90 * rutas.size / maximo).coerceAtLeast(6).dp)
                                    .width(28.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                            Text(
                                etiqueta,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PorTipo(todas: List<Ruta>) {
    val total = todas.size.coerceAtLeast(1)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Por tipo de ruta", fontWeight = FontWeight.SemiBold)
            TIPOS.forEach { tipo ->
                val cantidad = todas.count { it.tipo == tipo }
                val porcentaje = cantidad * 100 / total
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${tipoLabel(tipo)} · $cantidad rutas", style = MaterialTheme.typography.bodyMedium)
                        Text("$porcentaje%", fontWeight = FontWeight.Bold)
                    }
                    LinearProgressIndicator(
                        progress = { porcentaje / 100f },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp).height(6.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}
