package com.tecsup.gowaykiapp.ui.rutas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.gowaykiapp.data.model.Ruta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutasEnviadasScreen(
    state: RutasUiState,
    onBack: () -> Unit,
    onCrear: () -> Unit,
    onEditar: (Int) -> Unit,
    onFiltro: (String?) -> Unit,
    onEliminar: (Int) -> Unit,
    onReintentar: () -> Unit
) {
    var porEliminar by remember { mutableStateOf<Ruta?>(null) }
    val totalKm = state.rutas.sumOf { it.distanciaKm }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                "Rutas enviadas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = onCrear, contentPadding = PaddingValues(horizontal = 12.dp)) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(" Crear ruta", fontSize = 13.sp)
            }
        }

        Text(
            "${state.rutas.size} rutas · ${formatoKm(totalKm)} km",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = state.filtro == null,
                    onClick = { onFiltro(null) },
                    label = { Text("Todo") }
                )
            }
            items(ESTADOS) { estado ->
                FilterChip(
                    selected = state.filtro == estado,
                    onClick = { onFiltro(estado) },
                    label = { Text(estadoLabel(estado)) }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
            when {
                state.cargando && state.rutas.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null && state.rutas.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.error, color = MaterialTheme.colorScheme.error)
                        OutlinedButton(onClick = onReintentar, modifier = Modifier.padding(top = 12.dp)) {
                            Text("Reintentar")
                        }
                    }
                }

                state.rutas.isEmpty() -> {
                    Text(
                        "Todavía no hay rutas en esta categoría.",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.rutas, key = { it.id ?: it.hashCode() }) { ruta ->
                            RutaItem(
                                ruta = ruta,
                                onClick = { ruta.id?.let(onEditar) },
                                onEliminar = { porEliminar = ruta }
                            )
                        }
                    }
                }
            }
        }
    }

    porEliminar?.let { ruta ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("Eliminar ruta") },
            text = { Text("¿Seguro que quieres eliminar ${ruta.origen} → ${ruta.destino}?") },
            confirmButton = {
                TextButton(onClick = {
                    ruta.id?.let(onEliminar)
                    porEliminar = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { porEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun RutaItem(ruta: Ruta, onClick: () -> Unit, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    fechaCorta(ruta.fecha),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${ruta.origen} → ${ruta.destino}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(estadoColor(ruta.estado), CircleShape)
                    )
                    Text(
                        " ${estadoLabel(ruta.estado)} · ${tipoLabel(ruta.tipo)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = estadoColor(ruta.estado)
                    )
                }
            }
            Text(
                "${formatoKm(ruta.distanciaKm)} km",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
