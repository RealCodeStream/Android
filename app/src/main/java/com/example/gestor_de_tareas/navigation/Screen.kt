package com.example.gestor_de_tareas.navigation

sealed class Screen (val route: String) {
    object Home : Screen("carpetas")
    object Notifications : Screen("notifications")
    object Calendar : Screen("calendar")
    object Settings : Screen("settings")
    object FileList : Screen("fileList/{folderId}") {
        fun createRoute(folderId: Int) = "fileList/$folderId"
    }
    object FileDetail : Screen("fileDetail/{fileId}") {
        fun createRoute(fileId: Int) = "fileDetail/$fileId"
    }
}