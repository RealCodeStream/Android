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
class FileListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    fun getFilesForFolder(folderId: Int) = repository.getFilesForFolder(folderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addFile(file: File) {
        viewModelScope.launch {
            repository.insertFile(file)
        }
    }

    fun updateFile(file: File) {
        viewModelScope.launch {
            repository.updateFile(file)
        }
    }

    fun deleteFile(file: File) {
        viewModelScope.launch {
            repository.deleteFile(file)
        }
    }
}