package com.example.gestor_de_tareas.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.models.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileDetailViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    fun getFileById(fileId: Int) = repository.getFileById(fileId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateFile(file: File) {
        viewModelScope.launch {
            repository.updateFile(file)
        }
    }
}