package com.example.gestor_de_tareas.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.models.Folder
import com.example.gestor_de_tareas.navigation.Screen
import com.example.gestor_de_tareas.viewModel.FoldersViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun CarpetasScreen(
    navController: NavController,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var folderToEdit by remember { mutableStateOf<Folder?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Folder")
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
                    onClick = { navController.navigate(Screen.FileList.createRoute(folder.id)/*"files/${folder.id}"*/) },
                    onEdit = { folderToEdit = folder },
                    onDelete = { viewModel.deleteFolder(folder) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddEditFolderDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newFolder ->
                viewModel.addFolder(newFolder)
                showAddDialog = false
            }
        )
    }

    folderToEdit?.let { folder ->
        AddEditFolderDialog(
            folder = folder,
            onDismiss = { folderToEdit = null },
            onConfirm = { updatedFolder ->
                viewModel.updateFolder(updatedFolder)
                folderToEdit = null
            }
        )
    }
}

@Composable
fun FolderItem(folder: Folder,
               onClick: () -> Unit,
               onEdit: () -> Unit,
               onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column (modifier = Modifier.padding(16.dp)){
            Image(
                painter = rememberAsyncImagePainter(folder.imagePath),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Text(text = folder.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = folder.description, style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Folder")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Folder")
                }
            }
        }
    }
}