package com.example.gestor_de_tareas.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestor_de_tareas.models.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    val notifications = repository.getActiveNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(notificationId)
        }
    }
}