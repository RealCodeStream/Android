package com.example.gestor_de_tareas.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object Folders : Screen("folders")
    object FileList : Screen("filelist")
    object FileDetail : Screen("filedetail")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}
