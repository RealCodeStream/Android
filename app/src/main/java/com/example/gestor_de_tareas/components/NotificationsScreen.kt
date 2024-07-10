package com.example.gestor_de_tareas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gestor_de_tareas.ui.theme.Color1
import com.example.gestor_de_tareas.ui.theme.Color2
import com.example.gestor_de_tareas.ui.theme.Color3

@Composable
fun NotificationsScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        NotificationDialog(onDismiss = { showDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NotificationList()
    }
}

@Composable
fun NotificationList() {
    val notifications = remember { generateNotifications() }
    val colors = listOf(Color1, Color2, Color3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        notifications.forEachIndexed { index, notification ->
            NotificationItem(notification, colors[index % colors.size])
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, backgroundColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = notification.message,
            color = Color.Black,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = notification.timeAgo,
            color = Color.Black,
            fontSize = 12.sp
        )
    }
}

@Composable
fun NotificationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = { Text("Nueva Notificación") },
        text = { Text("Tienes una nueva notificación") },
        shape = RoundedCornerShape(16.dp)
    )
}

data class Notification(val message: String, val timeAgo: String)

fun generateNotifications(): List<Notification> {
    return listOf(
        Notification("Nueva Tarea: Desarrollo V", "2 minutes ago"),
        Notification("Nuevo Parcial: Ing.Software II", "10 minutes ago"),
        Notification("Nuevo mensaje de Dimas Concepcion", "15 minutes ago"),
        Notification("Recordatorio de Parcial 2", "1 hour ago")

    )
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen(navController = NavController(LocalContext.current))
}
