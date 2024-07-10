package com.example.gestor_de_tareas.navigation




import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gestor_de_tareas.components.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "carpetas", modifier = modifier) {
        composable("carpetas") { CarpetasScreen(navController) }
        composable("notificaciones") { NotificationsScreen(navController) }
        composable("calendario") { CalendarScreen(navController) }
        composable("configuracion") { SettingsScreen(navController) }
        composable("filelist/{folderId}") { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId")?.toInt() ?: 0
            FileListScreen(navController = navController, folderId = folderId)
        }
    }
}


