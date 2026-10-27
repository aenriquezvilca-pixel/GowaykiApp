package com.tecsup.gowaykiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.gowaykiapp.ui.navigation.GowaykiNav
import com.tecsup.gowaykiapp.ui.perfil.PerfilViewModel
import com.tecsup.gowaykiapp.ui.theme.GowaykiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Mismo ViewModel que usa la navegación (ambos cuelgan de la Activity)
            val perfilVm: PerfilViewModel = viewModel()
            val perfil by perfilVm.state.collectAsStateWithLifecycle()
            val oscuro = perfil.temaOscuro ?: isSystemInDarkTheme()

            if (!perfil.cargado) {
                // Mientras DataStore carga, solo un fondo neutro: evita el parpadeo del tema
                GowaykiAppTheme(darkTheme = isSystemInDarkTheme()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            } else {
                GowaykiAppTheme(darkTheme = oscuro) {
                    GowaykiNav(perfilVm = perfilVm)
                }
            }
        }
    }
}
