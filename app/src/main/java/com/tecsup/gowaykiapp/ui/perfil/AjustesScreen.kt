package com.tecsup.gowaykiapp.ui.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AjustesScreen(
    perfil: PerfilUiState,
    temaOscuroActual: Boolean,
    onTema: (Boolean) -> Unit,
    onNotificaciones: (Boolean) -> Unit,
    onBack: () -> Unit,
    onCerrarSesion: () -> Unit,
    onEliminarCuenta: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EncabezadoPantalla("Ajustes", onBack)

        Seccion("Apariencia") {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tema de la aplicación", fontWeight = FontWeight.SemiBold)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    FilterChip(
                        selected = !temaOscuroActual,
                        onClick = { onTema(false) },
                        label = { Text("Claro") }
                    )
                    FilterChip(
                        selected = temaOscuroActual,
                        onClick = { onTema(true) },
                        label = { Text("Oscuro") }
                    )
                }
            }
        }

        Seccion("Notificaciones") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Recibir notificaciones", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Avisos sobre el estado de tus rutas y respuestas de la comunidad.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = perfil.notificaciones, onCheckedChange = onNotificaciones)
            }
        }

        Seccion("Cuenta") {
            Column {
                FilaCuenta(
                    icono = Icons.AutoMirrored.Filled.Logout,
                    titulo = "Cerrar sesión",
                    subtitulo = "Puedes volver a entrar cuando quieras",
                    colorTitulo = MaterialTheme.colorScheme.onSurface,
                    onClick = onCerrarSesion
                )
                FilaCuenta(
                    icono = Icons.Default.DeleteOutline,
                    titulo = "Eliminar cuenta",
                    subtitulo = "Esta acción es irreversible",
                    colorTitulo = MaterialTheme.colorScheme.error,
                    onClick = onEliminarCuenta
                )
            }
        }
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            titulo.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) { contenido() }
    }
}

@Composable
private fun FilaCuenta(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    colorTitulo: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = colorTitulo)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(titulo, fontWeight = FontWeight.SemiBold, color = colorTitulo)
            Text(
                subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
