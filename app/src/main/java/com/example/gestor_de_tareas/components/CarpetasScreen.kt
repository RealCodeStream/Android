package com.example.gestor_de_tareas.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CarpetasScreen(navController: NavController) {
    var showAddFolderDialog by remember { mutableStateOf(false) }
    var folders by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddFolderDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir carpeta")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (folders.isEmpty()) {
                Text(
                    text = "Añade tus carpetas aquí",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                folders.forEach { folder ->
                    Text(text = folder, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }

    if (showAddFolderDialog) {
        AddFolderDialog(
            onDismiss = { showAddFolderDialog = false },
            onConfirm = { name ->
                folders = folders + name
                showAddFolderDialog = false
            }
        )
    }
}

@Composable
fun AddFolderDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text(text = "Nueva carpeta") },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") }
            )
        }
    )
}
