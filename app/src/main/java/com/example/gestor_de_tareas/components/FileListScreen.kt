package com.example.gestor_de_tareas.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.navigation.Screen
import com.example.gestor_de_tareas.viewModel.FileListViewModel

@Composable
fun FileListScreen(
    navController: NavController,
    folderId: Int,
    viewModel: FileListViewModel = hiltViewModel()
) {
    val files by viewModel.getFilesForFolder(folderId).collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var fileToEdit by remember { mutableStateOf<File?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add File")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(files) { file ->
                FileItem(
                    file = file,
                    onClick = { navController.navigate(Screen.FileDetail.createRoute(file.id)/*"fileDetail/${file.id}"*/) },
                    onEdit = { fileToEdit = file },
                    onDelete = { viewModel.deleteFile(file) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddEditFileDialog(
            file = null,
            folderId = folderId,
            onDismiss = { showAddDialog = false },
            onConfirm = { newFile ->
                viewModel.addFile(newFile)
                showAddDialog = false
            }
        )
    }

    fileToEdit?.let { file ->
        AddEditFileDialog(
            file = file,
            folderId = folderId,
            onDismiss = { fileToEdit = null },
            onConfirm = { updatedFile ->
                viewModel.updateFile(updatedFile)
                fileToEdit = null
            }
        )
    }
}

@Composable
fun FileItem(
    file: File,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = file.title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Due: ${DateUtils.formatDateTime(LocalContext.current, file.endDate, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit File")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete File")
                }
            }
        }
    }
}