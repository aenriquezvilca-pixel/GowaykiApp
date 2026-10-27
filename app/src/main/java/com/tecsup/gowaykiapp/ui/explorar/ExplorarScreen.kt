package com.tecsup.gowaykiapp.ui.explorar

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.gowaykiapp.data.model.Ruta
import com.tecsup.gowaykiapp.ui.rutas.RutasUiState
import com.tecsup.gowaykiapp.ui.rutas.TIPOS
import com.tecsup.gowaykiapp.ui.rutas.fechaCorta
import com.tecsup.gowaykiapp.ui.rutas.formatoKm
import com.tecsup.gowaykiapp.ui.rutas.tipoLabel

/** Rutas aprobadas por la comunidad, con búsqueda y filtro por tipo. */
@Composable
fun ExplorarScreen(
    state: RutasUiState,
    onVerRuta: (Int) -> Unit,
    onReintentar: () -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf<String?>(null) }

    val aprobadas = state.todas.filter { it.estado == "aprobada" }
    val visibles = aprobadas.filter { r ->
        (tipo == null || r.tipo == tipo) &&
            (busqueda.isBlank() ||
                r.origen.contains(busqueda, ignoreCase = true) ||
                r.destino.contains(busqueda, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(
            "Explorar",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            "Rutas aprobadas por la comunidad",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar origen o destino") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            item {
                FilterChip(selected = tipo == null, onClick = { tipo = null }, label = { Text("Todas") })
            }
            items(TIPOS) { t ->
                FilterChip(selected = tipo == t, onClick = { tipo = t }, label = { Text(tipoLabel(t)) })
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.cargando && state.todas.isEmpty() ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                state.error != null && state.todas.isEmpty() ->
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.error, color = MaterialTheme.colorScheme.error)
                        OutlinedButton(onClick = onReintentar, modifier = Modifier.padding(top = 12.dp)) {
                            Text("Reintentar")
                        }
                    }

                visibles.isEmpty() ->
                    Text(
                        "No hay rutas aprobadas que coincidan.",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                else ->
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(visibles, key = { it.id ?: it.hashCode() }) { ruta ->
                            RutaExplorarItem(ruta) { ruta.id?.let(onVerRuta) }
                        }
                    }
            }
        }
    }
}

@Composable
private fun RutaExplorarItem(ruta: Ruta, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${ruta.origen} → ${ruta.destino}", fontWeight = FontWeight.SemiBold)
                Text(
                    "${tipoLabel(ruta.tipo)} · ${fechaCorta(ruta.fecha)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "${formatoKm(ruta.distanciaKm)} km",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
