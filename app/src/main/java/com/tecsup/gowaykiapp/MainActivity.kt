package com.tecsup.gowaykiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
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

            GowaykiAppTheme(darkTheme = oscuro) {
                GowaykiNav(perfilVm = perfilVm)
            }
        }
    }
}
