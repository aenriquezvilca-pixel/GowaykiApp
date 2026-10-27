package com.tecsup.gowaykiapp.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun EditarPerfilScreen(
    perfil: PerfilUiState,
    onGuardar: (nombre: String, correo: String, ciudad: String, telefono: String, descripcion: String) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(perfil.nombre) }
    var correo by remember { mutableStateOf(perfil.correo) }
    var ciudad by remember { mutableStateOf(perfil.ciudad) }
    var telefono by remember { mutableStateOf(perfil.telefono) }
    var descripcion by remember { mutableStateOf(perfil.descripcion) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EncabezadoPantalla("Editar perfil", onCancelar)

        Text(
            "Así te verá la comunidad. Tu correo no se muestra en tu perfil público.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre completo") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            label = { Text("Correo electrónico") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ciudad, onValueChange = { ciudad = it },
            label = { Text("Ciudad") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono, onValueChange = { telefono = it },
            label = { Text("Número de teléfono") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = descripcion, onValueChange = { descripcion = it },
            label = { Text("Sobre ti") }, minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                error = when {
                    nombre.isBlank() -> "Escribe tu nombre."
                    !correo.contains("@") -> "Escribe un correo válido."
                    else -> null
                }
                if (error == null) {
                    onGuardar(nombre.trim(), correo.trim(), ciudad.trim(), telefono.trim(), descripcion.trim())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar cambios") }

        OutlinedButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) { Text("Cancelar") }
    }
}
