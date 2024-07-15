package com.example.gestor_de_tareas.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.viewModel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val darkMode by viewModel.darkModeEnabled.observeAsState(initial = false)
    val notificationsEnabled by viewModel.notificationsEnabled.observeAsState(initial = true)

    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Mode")
                Switch(
                    checked = darkMode,
                    onCheckedChange = { viewModel.updateDarkModeSettings(it) }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Notifications")
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.updateNotificationSettings(it) }
                )
            }
            Button(
                onClick = { viewModel.clearAllData() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Clear All Data")
            }
        }
    }
}