package com.example.gestor_de_tareas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestor_de_tareas.models.Folder
import com.example.gestor_de_tareas.viewModel.FoldersViewModel

@Composable
fun CarpetasScreen(
    navController: NavController,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var folderName by remember { mutableStateOf("") }
    var folderDescription by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (folders.isEmpty()) {
                Text(
                    text = "Añade tus carpetas aquí",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(folders) { folder ->
                        FolderItem(
                            folder = folder,
                            onClick = { navController.navigate("filelist/${folder.id}") }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Nueva carpeta") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = folderName,
                            onValueChange = { folderName = it },
                            label = { Text("Nombre") }
                        )
                        OutlinedTextField(
                            value = folderDescription,
                            onValueChange = { folderDescription = it },
                            label = { Text("Descripción") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addFolder(Folder(0, folderName, folderDescription, ""))
                            showDialog = false
                        }
                    ) {
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
    }
}

@Composable
fun FolderItem(folder: Folder, onClick: () -> Unit) {
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
                // Placeholder for image
            }
            Text(text = folder.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = folder.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
