package com.example.gestor_de_tareas.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.viewModel.FileListViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileListScreen(navController: NavController, folderId: Int) {
    val viewModel: FileListViewModel = hiltViewModel()
    val files by viewModel.getFilesForFolder(folderId).collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var newFileTitle by remember { mutableStateOf("") }
    var newFileContent by remember { mutableStateOf("") }
    var newFileAttachmentUri by remember { mutableStateOf<Uri?>(null) }
    var newFileDate by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            newFileAttachmentUri = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Archivos en la carpeta $folderId") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Archivo Agregado")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            LazyColumn {
                items(files) { file ->
                    FileItem(file, onDelete = {
                        coroutineScope.launch {
                            viewModel.deleteFile(it)
                            snackbarHostState.showSnackbar("Archivo borrado")
                        }
                    })
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Agrega un archivo") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newFileTitle,
                                onValueChange = { newFileTitle = it },
                                label = { Text("Nombre") }
                            )
                            OutlinedTextField(
                                value = newFileContent,
                                onValueChange = { newFileContent = it },
                                label = { Text("Descripcion") }
                            )
                            OutlinedTextField(
                                value = newFileDate,
                                onValueChange = { newFileDate = it },
                                label = { Text("Fecha") }
                            )
                            Button(
                                onClick = {
                                    filePickerLauncher.launch("image/*") // Puedes ajustar el tipo MIME según el tipo de archivo que deseas permitir
                                }
                            ) {
                                Text("Subir Archivo")
                            }
                            newFileAttachmentUri?.let { uri ->
                                Text("Selecciona Archivo: $uri")
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                newFileAttachmentUri?.let { uri ->
                                    val newFile = File(
                                        folderId = folderId,
                                        title = newFileTitle,
                                        content = newFileContent,
                                        attachmentPath = uri.toString(), // Guardar URI como String
                                        date = newFileDate // Usar el valor de la fecha ingresada
                                    )
                                    coroutineScope.launch {
                                        viewModel.addFile(newFile)
                                        showDialog = false
                                        newFileTitle = ""
                                        newFileContent = ""
                                        newFileAttachmentUri = null // Reiniciar el URI después de agregar el archivo
                                        newFileDate = ""
                                        snackbarHostState.showSnackbar("Archivo Agregado")
                                    }
                                }
                            }
                        ) {
                            Text("Agregar")
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
}

@Composable
fun FileItem(file: File, onDelete: (File) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(file.title, style = MaterialTheme.typography.titleLarge)
                Text(file.content, style = MaterialTheme.typography.titleSmall)
                Text("Date: ${file.date}")
            }
            IconButton(onClick = { onDelete(file) }) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar Archivo")
            }
        }
    }
}
