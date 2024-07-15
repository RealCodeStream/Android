package com.example.gestor_de_tareas.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.models.CalendarEvent
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.viewModel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel()
) {

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedDateMillis = remember(selectedDate) {
        selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val tasks by viewModel.getTasksForDate(selectedDateMillis/*selectedDate.toEpochDay() * 86400000*/).collectAsState(initial = emptyList())
    var showAddEventDialog by remember { mutableStateOf(false) }
    val events by viewModel.getEventsForDate(selectedDateMillis).collectAsState(initial = emptyList())


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddEventDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DatePicker(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                item {
                    Text(
                        "Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(tasks) { task ->
                    TaskItem(task = task)
                }
                item {
                    Text(
                        "Events",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(events) { event ->
                    EventItem(event = event)
                }
            }
        }
    }

    if (showAddEventDialog) {
        AddEventDialog(
            date = selectedDate,
            onDismiss = { showAddEventDialog = false },
            onConfirm = { newEvent ->
                viewModel.addEvent(newEvent)
                showAddEventDialog = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onDateSelected(selectedDate.minusDays(1)) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Day")
        }
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
            style = MaterialTheme.typography.headlineSmall
        )
        IconButton(onClick = { onDateSelected(selectedDate.plusDays(1)) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Day")
        }
    }
}

@Composable
fun TaskItem(task: File) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun EventItem(event: CalendarEvent) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventDialog(
    date: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (CalendarEvent) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Event") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val startOfDay = date.atStartOfDay(ZoneId.systemDefault())
                val endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1)
                val newEvent = CalendarEvent(
                    id = 0,
                    title = title,
                    description = description,
                    startDate = startOfDay.toInstant().toEpochMilli(),
                    endDate = endOfDay.toInstant().toEpochMilli()
                )
                onConfirm(newEvent)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}