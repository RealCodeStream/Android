package com.example.gestor_de_tareas.components

import androidx.navigation.NavController
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.navigation.NavHostController
import com.example.gestor_de_tareas.ui.theme.Blue1
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CheckCircle
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import java.util.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult



@Composable
fun CalendarScreen(navController: NavController) {
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val events = remember {
        mutableStateOf(
            mapOf(
                LocalDate.of(YearMonth.now().year, 7, 5) to listOf("Tarea de Contabilidad")
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentMonth.value = currentMonth.value.minusMonths(1)
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
                }
                Box {
                    Text(
                        text = currentMonth.value.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        ) + " " + currentMonth.value.year,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
                IconButton(onClick = {
                    currentMonth.value = currentMonth.value.plusMonths(1)
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CalendarView(currentMonth.value, selectedDate.value, events.value) { date ->
                selectedDate.value = date
               navController.navigate("carpetas")
            }
        }
        Example(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    events: Map<LocalDate, List<String>>,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7
    val weeks = (daysInMonth + firstDayOfWeek - 1) / 7 + 1

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        for (week in 0 until weeks) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (day in 0..6) {
                    val dayOfMonth = week * 7 + day - firstDayOfWeek + 1
                    if (dayOfMonth in 1..daysInMonth) {
                        DayButton(
                            day = dayOfMonth,
                            isSelected = selectedDate == yearMonth.atDay(dayOfMonth),
                            events = events[yearMonth.atDay(dayOfMonth)] ?: emptyList(),
                            onClick = { onDateSelected(yearMonth.atDay(dayOfMonth)) }
                        )
                    } else {
                        Spacer(modifier = Modifier.size(45.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DayButton(day: Int, isSelected: Boolean, events: List<String>, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(2.5.dp)
            .size(48.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Blue1,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Text(
            text = day.toString(),
            color = if (isSelected) Color.White else Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        events.take(2).forEach { event ->
            Text(
                text = event,
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun Example(modifier: Modifier = Modifier) {
    var showDialogWithImage by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showCardCalendarForm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { expanded = true },
            modifier = modifier
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button.")
        }
        Spacer(modifier = Modifier.height(4.dp))

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Evento") },
                onClick = {
                    showCardCalendarForm = true
                    expanded = false
                },
                leadingIcon = {
                    Icon(Icons.Filled.DateRange, contentDescription = null)
                }
            )
            DropdownMenuItem(
                text = { Text("Tarea") },
                onClick = {
                    showDialogWithImage = true
                    expanded = false
                },
                leadingIcon = {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                }
            )
        }
        if (showDialogWithImage) {
            DialogWithImage(
                onDismissRequest = { showDialogWithImage = false },
            )
        }
        if (showCardCalendarForm) {
            CardCalendarForm(
                onDismissRequest = { showCardCalendarForm = false }
            )
        }
    }
}

@Composable
fun DialogWithImage(onDismissRequest: () -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var isChecked by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Agregar Título") },
                    placeholder = { Text("Ingrese el título") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detalles de Tarea") },
                    placeholder = { Text("Ingrese la descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Todo el Día")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            if (it) {
                                val calendar = Calendar.getInstance()
                                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                                val minute = calendar.get(Calendar.MINUTE)
                                selectedTime = "$hour:$minute"
                            } else {
                                selectedTime = ""
                            }
                        }
                    )
                }
                if (!isChecked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH)
                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                                        selectedDate =
                                            "$selectedDay/${selectedMonth + 1}/$selectedYear"
                                    },
                                    year,
                                    month,
                                    day
                                ).show()
                            },
                        ) {
                            Text("Seleccionar Fecha")
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(": $selectedDate")
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                                val minute = calendar.get(Calendar.MINUTE)

                                TimePickerDialog(
                                    context,
                                    { _: android.widget.TimePicker, selectedHour: Int, selectedMinute: Int ->
                                        selectedTime = "$selectedHour:$selectedMinute"
                                    },
                                    hour,
                                    minute,
                                    true
                                ).show()
                            }
                        ) {
                            Text("Seleccionar Hora")
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(": $selectedTime")
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH)
                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                                        selectedDate =
                                            "$selectedDay/${selectedMonth + 1}/$selectedYear"
                                    },
                                    year,
                                    month,
                                    day
                                ).show()
                            },
                        ) {
                            Text("Seleccionar Fecha")
                        }
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(": $selectedDate")
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}
@Composable
fun CardCalendarForm(onDismissRequest: () -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var isChecked by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fileUri = result.data?.data
        }
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Agregar Título") },
                    placeholder = { Text("Ingrese el título") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detalles de Tarea") },
                    placeholder = { Text("Ingrese la descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Todo el Día")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            if (it) {
                                val calendar = Calendar.getInstance()
                                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                                val minute = calendar.get(Calendar.MINUTE)
                                selectedTime = "$hour:$minute"
                            } else {
                                selectedTime = ""
                            }
                        }
                    )
                }
                if (!isChecked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH)
                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                                        selectedDate =
                                            "$selectedDay/${selectedMonth + 1}/$selectedYear"
                                    },
                                    year,
                                    month,
                                    day
                                ).show()
                            },
                        ) {
                            Text("Seleccionar Fecha")
                        }
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(": $selectedDate")
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                                val minute = calendar.get(Calendar.MINUTE)

                                TimePickerDialog(
                                    context,
                                    { _: android.widget.TimePicker, selectedHour: Int, selectedMinute: Int ->
                                        selectedTime = "$selectedHour:$selectedMinute"
                                    },
                                    hour,
                                    minute,
                                    true
                                ).show()
                            }
                        ) {
                            Text("Seleccionar Hora")
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(": $selectedTime")
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH)
                                val day = calendar.get(Calendar.DAY_OF_MONTH)


                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                                        selectedDate =
                                            "$selectedDay/${selectedMonth + 1}/$selectedYear"
                                    },
                                    year,
                                    month,
                                    day
                                ).show()
                            },
                        ) {
                            Text("Seleccionar Fecha")
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(": $selectedDate")
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                        }
                        launcher.launch(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Subir Archivo")
                }

                fileUri?.let {
                    Text(text = "Archivo seleccionado: $it")
                }
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Guardar")
                }
                UploadFileButton(launcher = launcher, fileUri = fileUri)
            }
        }
    }
}

@Composable
fun UploadFileButton(launcher: ActivityResultLauncher<Intent>, fileUri: Uri?) {
    Column(modifier = Modifier.padding(12.dp)) {
        fileUri?.let { uri ->
            Text(
                text = "Archivo seleccionado: ${uri.path}",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
