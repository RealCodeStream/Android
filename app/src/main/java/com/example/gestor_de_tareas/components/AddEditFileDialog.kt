package com.example.gestor_de_tareas.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.gestor_de_tareas.models.File

@Composable
fun AddEditFileDialog(
    file: File? = null,
    folderId: Int,
    onDismiss: () -> Unit,
    onConfirm: (File) -> Unit
) {
    var title by remember { mutableStateOf(file?.title ?: "") }
    var content by remember { mutableStateOf(file?.content ?: "") }
    var endDate by remember { mutableLongStateOf(file?.endDate ?: System.currentTimeMillis()) }
    //val context = LocalContext.current


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (file == null) "Add File" else "Edit File") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                TextField(value = content, onValueChange = { content = it }, label = { Text("Content") })
                // Aqui puedes añadir un DatePicker para seleccionar endDate
            }
        },
        confirmButton = {
            Button(onClick = {
                val newFile = File(
                    id = file?.id ?: 0,
                    folderId = folderId,
                    title = title,
                    content = content,
                    attachmentPath = file?.attachmentPath,
                    startDate = file?.startDate ?: System.currentTimeMillis(),
                    endDate = endDate
                )
                onConfirm(newFile)
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

/*@Composable
fun AddEditFileDialog2(
    file: File?,
    folderId: Int,
    onDismiss: () -> Unit,
    onConfirm: (File) -> Unit
) {
    var title by remember { mutableStateOf(file?.title ?: "") }
    var content by remember { mutableStateOf(file?.content ?: "") }
    var endDate by remember { mutableStateOf(file?.endDate ?: System.currentTimeMillis()) }
    val context = LocalContext.current

    fun openDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                endDate = calendar.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 24.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = if (file == null) "Add File" else "Edit File", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = content, onValueChange = { content = it }, label = { Text("Content") })
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Due Date: ${DateUtils.formatDateTime(context, endDate, DateUtils.FORMAT_SHOW_DATE)}")
                    Button(onClick = { openDatePicker() }) {
                        Text("Select Date")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        onConfirm(File(id = file?.id ?: 0, title = title, content = content, endDate = endDate, folderId = folderId))
                    }) { Text("Save") }
                }
            }
        }
    }
}*/