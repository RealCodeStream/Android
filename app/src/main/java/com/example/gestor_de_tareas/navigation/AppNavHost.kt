package com.example.gestor_de_tareas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gestor_de_tareas.components.*

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "carpetas", modifier = modifier) {
        composable("carpetas") { CarpetasScreen(navController) }
        composable("notificaciones") { NotificationsScreen(navController) }
        composable("calendario") { CalendarScreen(navController) }
        composable("configuracion") { SettingsScreen(navController) }
        composable("filelist/{folderId}") { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId")?.toInt() ?: return@composable
            FileListScreen(navController, folderId)
        }
    }
}
