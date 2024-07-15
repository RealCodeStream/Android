package com.example.gestor_de_tareas

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestor_de_tareas.components.CalendarScreen
import com.example.gestor_de_tareas.components.CarpetasScreen
import com.example.gestor_de_tareas.components.FileDetailScreen
import com.example.gestor_de_tareas.components.FileListScreen
import com.example.gestor_de_tareas.components.NotificationsScreen
import com.example.gestor_de_tareas.components.SettingsScreen
import com.example.gestor_de_tareas.navigation.Screen
import com.example.gestor_de_tareas.ui.theme.Gestor_de_TareasTheme
import com.example.gestor_de_tareas.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val darkMode by viewModel.darkModeEnabled.collectAsState(initial = isSystemInDarkTheme())
            Gestor_de_TareasTheme(darkTheme = darkMode) {
                val items = listOf(
                    NavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        route = Screen.Home.route
                    ),
                    NavigationItem(
                        title = "Notifications",
                        selectedIcon = Icons.Filled.Notifications,
                        unselectedIcon = Icons.Outlined.Notifications,
                        //badgeCount = 45,
                        route = Screen.Notifications.route
                    ),
                    NavigationItem(
                        title = "Calendar",
                        selectedIcon = Icons.Filled.DateRange,
                        unselectedIcon = Icons.Outlined.DateRange,
                        //badgeCount = 45,
                        route = Screen.Calendar.route
                    ),
                    NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        route = Screen.Settings.route
                        /**/
                    ),
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(text = item.title)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                           navController.navigate(item.route)
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        badge = {
                                            item.badgeCount?.let {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(

                                    title = {
                                        Text(text = "ultimate task",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis)

                                    },

                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu"
                                            )

                                        }


                                    },
                                    actions = {
                                        IconButton(onClick = {navController.navigate(Screen.Notifications.route) }) {
                                            Icon(
                                                imageVector = Icons.Filled.Notifications,
                                                contentDescription = "Notificaciones"
                                            )
                                        }
                                    }
                                )

                            },

                        ) { innerPadding ->
                            NavHost(navController = navController,
                                startDestination = Screen.Home.route,
                                modifier = Modifier.padding(innerPadding)) {
                                composable(Screen.Home.route){ CarpetasScreen(navController) }
                                composable(Screen.Notifications.route){ NotificationsScreen(navController) }
                                composable(Screen.Calendar.route){ CalendarScreen(navController) }
                                composable(Screen.Settings.route) {  SettingsScreen(navController) }
                                composable(Screen.FileList.route,
                                    arguments = listOf(navArgument("folderId") { type = NavType.IntType }))
                                {backStackEntry ->
                                    FileListScreen(
                                        navController,
                                        folderId = backStackEntry.arguments?.getInt("folderId") ?: -1
                                    )
                                }
                                composable(Screen.FileDetail.route,
                                    arguments = listOf(navArgument("fileId") { type = NavType.IntType }))
                                {backStackEntry ->
                                    FileDetailScreen(
                                        navController,
                                        fileId = backStackEntry.arguments?.getInt("fileId") ?: -1
                                    )
                                }
                                
                            }

                        }
                    }
                }

            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String
)

