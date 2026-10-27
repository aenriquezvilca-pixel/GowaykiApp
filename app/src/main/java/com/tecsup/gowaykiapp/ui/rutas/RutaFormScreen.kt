package com.tecsup.gowaykiapp.ui.rutas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.gowaykiapp.data.model.Ruta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Crear (existente == null) o editar una ruta. */
@Composable
fun RutaFormScreen(
    existente: Ruta?,
    guardando: Boolean,
    errorServidor: String?,
    onGuardar: (Ruta) -> Unit,
    onCancelar: () -> Unit
) {
    val hoy = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }

    var origen by remember(existente) { mutableStateOf(existente?.origen ?: "") }
    var destino by remember(existente) { mutableStateOf(existente?.destino ?: "") }
    var distancia by remember(existente) {
        mutableStateOf(existente?.distanciaKm?.toString() ?: "")
    }
    var fecha by remember(existente) { mutableStateOf(existente?.fecha ?: hoy) }
    var tipo by remember(existente) { mutableStateOf(existente?.tipo ?: "peatonal") }
    var descripcion by remember(existente) { mutableStateOf(existente?.descripcion ?: "") }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    fun validarYGuardar() {
        val km = distancia.replace(',', '.').toDoubleOrNull()
        errorLocal = when {
            origen.isBlank() -> "Escribe el origen."
            destino.isBlank() -> "Escribe el destino."
            km == null || km <= 0 -> "La distancia debe ser un número mayor que 0."
            !Regex("""\d{4}-\d{2}-\d{2}""").matches(fecha) -> "La fecha debe tener el formato AAAA-MM-DD."
            else -> null
        }
        if (errorLocal == null && km != null) {
            onGuardar(
                Ruta(
                    id = existente?.id,
                    origen = origen.trim(),
                    destino = destino.trim(),
                    distanciaKm = km,
                    fecha = fecha,
                    estado = existente?.estado ?: "en_revision",
                    tipo = tipo,
                    descripcion = descripcion.trim()
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancelar) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                if (existente == null) "Crear ruta" else "Editar ruta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedTextField(
            value = origen, onValueChange = { origen = it },
            label = { Text("Origen") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = destino, onValueChange = { destino = it },
            label = { Text("Destino") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = distancia, onValueChange = { distancia = it },
            label = { Text("Distancia (km)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fecha, onValueChange = { fecha = it },
            label = { Text("Fecha (AAAA-MM-DD)") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Tipo de ruta", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TIPOS.forEach { t ->
                FilterChip(
                    selected = tipo == t,
                    onClick = { tipo = t },
                    label = { Text(tipoLabel(t)) }
                )
            }
        }

        OutlinedTextField(
            value = descripcion, onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        val mensaje = errorLocal ?: errorServidor
        if (mensaje != null) {
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { validarYGuardar() },
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (guardando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(if (existente == null) "Guardar ruta" else "Guardar cambios")
            }
        }
        OutlinedButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Text("Cancelar")
        }
    }
}
