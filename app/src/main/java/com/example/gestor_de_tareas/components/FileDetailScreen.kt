package com.example.gestor_de_tareas.components

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.viewModel.FileDetailViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FileDetailScreen(
    navController: NavController,
    fileId: Int,
    viewModel: FileDetailViewModel = hiltViewModel()
) {
    val file by viewModel.getFileById(fileId).collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf("") }
    var editedContent by remember { mutableStateOf("") }
    var editedEndDate by remember { mutableLongStateOf(0L) }
    //var showDatePicker by remember { mutableStateOf(false) }

    file?.let { currentFile ->
        LaunchedEffect(currentFile) {
            editedTitle = currentFile.title
            editedContent = currentFile.content
            editedEndDate = currentFile.endDate
        }

        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(if (isEditing) "Edit File" else "File Details")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isEditing) {
                        IconButton(onClick = {
                            viewModel.updateFile(currentFile.copy(
                                //title = editedTitle,
                                content = editedContent,
                                endDate = editedEndDate
                            ))
                            isEditing = false
                        }) {
                            Icon(Icons.Filled.Done, contentDescription = "Save")
                        }

                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                    }
                }

               // if (isEditing) {
                   /* TextField(
                        value = editedTitle,
                        onValueChange = { editedTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )*/
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = editedContent,
                        onValueChange = { editedContent = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Aqui se puede añadir un DatePicker para editar editedEndDate
                /*} else {*/
                    Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = { navController.navigateUp() }) {
                        Text("Back")
                    }
                    Text(
                        text = "Due: ${DateUtils.formatDateTime(LocalContext.current, currentFile.endDate, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                }

                //}
            }
        }
    }
}


/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FileDetailScreen(
    navController: NavController,
    fileId: Int,
    viewModel: FileDetailViewModel = hiltViewModel()
) {
    val file by viewModel.getFileById(fileId).collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf("") }
    var editedContent by remember { mutableStateOf("") }
    var editedEndDate by remember { mutableLongStateOf(0L) }
    var showDatePicker by remember { mutableStateOf(false) }

    file?.let { currentFile ->
        LaunchedEffect(currentFile) {
            editedTitle = currentFile.title
            editedContent = currentFile.content
            editedEndDate = currentFile.endDate
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //if (isEditing) {
                TextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedContent,
                    onValueChange = { editedContent = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { showDatePicker = true }) {
                    Text("Set Due Date")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        viewModel.updateFile(currentFile.copy(
                            title = editedTitle,
                            content = editedContent,
                            endDate = editedEndDate
                        ))
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                }
           /* } else {*/
                Text(text = currentFile.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = currentFile.content, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Due: ${DateUtils.formatDateTime(LocalContext.current, currentFile.endDate, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                /*Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }*/
           // }
        }

        if (showDatePicker) {
            CustomDatePicker(
                selectedDate = editedEndDate,
                onDateSelected = { newDate ->
                    editedEndDate = newDate
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let(onDateSelected)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}*/
/*fun DatePicker(state: DatePickerState) {


}*/
