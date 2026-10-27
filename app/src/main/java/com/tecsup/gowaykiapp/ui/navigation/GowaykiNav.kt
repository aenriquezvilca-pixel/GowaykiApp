package com.tecsup.gowaykiapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.gowaykiapp.ui.estadisticas.EstadisticasScreen
import com.tecsup.gowaykiapp.ui.perfil.PerfilScreen
import com.tecsup.gowaykiapp.ui.rutas.RutaFormScreen
import com.tecsup.gowaykiapp.ui.rutas.RutasEnviadasScreen
import com.tecsup.gowaykiapp.ui.rutas.RutasViewModel

object Rutas {
    const val EXPLORAR = "explorar"
    const val PERFIL = "perfil"
    const val ESTADISTICAS = "estadisticas"
    const val RUTAS_ENVIADAS = "rutas_enviadas"
    const val FORM = "ruta_form/{id}"
    fun form(id: Int?) = "ruta_form/${id ?: -1}"
}

@Composable
fun GowaykiNav(vm: RutasViewModel = viewModel()) {
    val nav = rememberNavController()
    val state by vm.state.collectAsStateWithLifecycle()
    val entrada by nav.currentBackStackEntryAsState()
    val actual = entrada?.destination?.route

    val conBarra = actual in listOf(Rutas.EXPLORAR, Rutas.PERFIL, Rutas.ESTADISTICAS, Rutas.RUTAS_ENVIADAS)

    Scaffold(
        bottomBar = {
            if (conBarra) {
                NavigationBar {
                    NavigationBarItem(
                        selected = actual == Rutas.EXPLORAR,
                        onClick = {
                            nav.navigate(Rutas.EXPLORAR) { launchSingleTop = true }
                        },
                        icon = { Icon(Icons.Default.Explore, contentDescription = null) },
                        label = { Text("Explorar") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { nav.navigate(Rutas.form(null)) },
                        icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
                        label = { Text("Contribuir") }
                    )
                    NavigationBarItem(
                        selected = actual != Rutas.EXPLORAR,
                        onClick = {
                            nav.navigate(Rutas.PERFIL) {
                                popUpTo(Rutas.PERFIL) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Mi perfil") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Rutas.PERFIL,
            modifier = Modifier.padding(padding).statusBarsPadding()
        ) {
            composable(Rutas.EXPLORAR) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Explorar (próximamente)")
                }
            }
            composable(Rutas.PERFIL) {
                PerfilScreen(
                    state = state,
                    onEstadisticas = { nav.navigate(Rutas.ESTADISTICAS) },
                    onRutasEnviadas = { nav.navigate(Rutas.RUTAS_ENVIADAS) },
                    onEditarPerfil = { /* siguiente etapa */ },
                    onAjustes = { /* siguiente etapa */ }
                )
            }
            composable(Rutas.ESTADISTICAS) {
                EstadisticasScreen(state = state, onBack = { nav.popBackStack() })
            }
            composable(Rutas.RUTAS_ENVIADAS) {
                RutasEnviadasScreen(
                    state = state,
                    onBack = { nav.popBackStack() },
                    onCrear = { nav.navigate(Rutas.form(null)) },
                    onEditar = { id -> nav.navigate(Rutas.form(id)) },
                    onFiltro = vm::cambiarFiltro,
                    onEliminar = vm::eliminar,
                    onReintentar = vm::cargar
                )
            }
            composable(
                route = Rutas.FORM,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                val existente = state.todas.find { it.id == id }
                RutaFormScreen(
                    existente = existente,
                    guardando = state.guardando,
                    errorServidor = state.error,
                    onGuardar = { ruta -> vm.guardar(ruta) { nav.popBackStack() } },
                    onCancelar = {
                        vm.limpiarError()
                        nav.popBackStack()
                    }
                )
            }
        }
    }
}
