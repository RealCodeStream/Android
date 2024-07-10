package com.example.gestor_de_tareas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.models.Folder
import com.example.gestor_de_tareas.viewModel.FoldersViewModel

@Composable
fun CarpetasScreen(
    navController: NavController,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var folderTitle by remember { mutableStateOf("") }
    var folderDescription by remember { mutableStateOf("") }
    var folderToDelete by remember { mutableStateOf<Folder?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nueva carpeta") },
            text = {
                Column {
                    TextField(
                        value = folderTitle,
                        onValueChange = { folderTitle = it },
                        label = { Text("Nombre") }
                    )
                    TextField(
                        value = folderDescription,
                        onValueChange = { folderDescription = it },
                        label = { Text("Descripción") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addFolder(Folder(0, folderTitle, folderDescription, ""))
                    folderTitle = ""
                    folderDescription = ""
                    showDialog = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (folderToDelete != null) {
        AlertDialog(
            onDismissRequest = { folderToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta carpeta?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteFolder(folderToDelete!!)
                    folderToDelete = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { folderToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar carpeta")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues
        ) {
            items(folders) { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { navController.navigate("files/${folder.id}") },
                    onDelete = { folderToDelete = folder }
                )
            }
        }
    }
}

@Composable
fun FolderItem(folder: Folder, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Gray)
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar carpeta")
                }
            }
            Text(text = folder.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = folder.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
