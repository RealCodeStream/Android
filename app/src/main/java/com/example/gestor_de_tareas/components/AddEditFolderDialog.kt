package com.example.gestor_de_tareas.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gestor_de_tareas.models.Folder

@Composable
fun AddEditFolderDialog(
    folder: Folder? = null,
    onDismiss: () -> Unit,
    onConfirm: (Folder) -> Unit
) {
    var title by remember { mutableStateOf(folder?.title ?: "") }
    var description by remember { mutableStateOf(folder?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (folder == null) "Add Folder" else "Edit Folder") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(Folder(id = folder?.id ?: 0, title = title, description = description, imagePath = folder?.imagePath))
                onDismiss()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}