package com.example.gestor_de_tareas.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestor_de_tareas.models.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    val darkModeEnabled = repository.darkModeEnabled.asLiveData()
    val notificationsEnabled = repository.notificationsEnabled.asLiveData()

    fun updateDarkModeSettings(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateDarkModeSettings(enabled)
        }
    }

    fun updateNotificationSettings(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateNotificationSettings(enabled)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}