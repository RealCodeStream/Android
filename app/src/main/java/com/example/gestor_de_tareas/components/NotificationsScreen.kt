package com.example.gestor_de_tareas.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gestor_de_tareas.models.Notification
import com.example.gestor_de_tareas.viewModel.NotificationsViewModel

@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState(/*initial = emptyList()*/)

    Scaffold(
    ) { paddingValues ->
        Spacer(modifier = Modifier.height(8.dp))
        /*Button(onClick = { navController.navigateUp() }) {
            Text("Back")
        }*/
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(notifications)  { notification ->
                NotificationItem(
                    notification = notification,
                    onMarkAsRead = { viewModel.markAsRead(notification.id) }
                )
            }
        }


    }
}

@Composable
fun NotificationItem(notification: Notification, onMarkAsRead: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = notification.message, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Due: ${DateUtils.getRelativeTimeSpanString(notification.dueDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onMarkAsRead) {
                Text("Mark as Read")
            }
        }
    }
}
