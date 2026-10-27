package com.tecsup.gowaykiapp.ui.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EliminarCuentaScreen(
    perfil: PerfilUiState,
    totalRutas: Int,
    eliminando: Boolean,
    error: String?,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding().padding(horizontal = 16.dp)
    ) {
        EncabezadoPantalla("Eliminar cuenta", onCancelar)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text("¿Eliminar tu cuenta?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                "${perfil.nombre.substringBefore(" ")}, eliminarás tu perfil de GoWayki Pulse y perderás el acceso a tus contribuciones.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.10f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Esta acción es irreversible",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text("✕  Se eliminarán tus datos de perfil")
                    Text("✕  Perderás el historial de $totalRutas rutas enviadas")
                    Text("✕  No podrás recuperar esta cuenta")
                }
            }

            Text(
                "Si solo necesitas un descanso, puedes cerrar sesión y conservar tu cuenta.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }

        OutlinedButton(
            onClick = onCancelar,
            enabled = !eliminando,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cancelar") }
        Button(
            onClick = onConfirmar,
            enabled = !eliminando,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)
        ) {
            if (eliminando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
            } else {
                Text("Eliminar mi cuenta definitivamente")
            }
        }
    }
}
