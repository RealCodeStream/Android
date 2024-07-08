package com.example.gestor_de_tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.gestor_de_tareas.navigation.AppNavHost
import com.example.gestor_de_tareas.ui.theme.Gestor_de_TareasTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Gestor_de_TareasTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Gestor de tareas", style = MaterialTheme.typography.headlineSmall)
                                Spacer(modifier = Modifier.height(16.dp))
                                NavigationItem("Carpetas", Icons.Default.Menu) {
                                    navController.navigate("carpetas")
                                    scope.launch { drawerState.close() }
                                }
                                NavigationItem("Notificaciones", Icons.Default.Notifications) {
                                    navController.navigate("notificaciones")
                                    scope.launch { drawerState.close() }
                                }
                                NavigationItem("Calendario", Icons.Default.DateRange) {
                                    navController.navigate("calendario")
                                    scope.launch { drawerState.close() }
                                }
                                NavigationItem("Configuracion", Icons.Default.Settings) {
                                    navController.navigate("configuracion")
                                    scope.launch { drawerState.close() }
                                }
                            }
                        }
                    },
                    drawerState = drawerState
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Gestor de Tareas") },
                                navigationIcon = {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                }
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(onClick = { /* Acción del FAB */ }) {
                                Icon(Icons.Default.Add, contentDescription = "Agregar")
                            }
                        }
                    ) { paddingValues ->
                        AppNavHost(navController = navController, modifier = Modifier.padding(paddingValues))
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .padding(vertical = 4.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label)
        }
    }
}
