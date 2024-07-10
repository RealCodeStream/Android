package com.example.gestor_de_tareas.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.models.Folder
import com.example.gestor_de_tareas.models.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val folders = repository.getAllFolders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addFolder(folder: Folder) {
        viewModelScope.launch {
            repository.insertFolder(folder)
        }
    }


    // Función para obtener los archivos por ID de carpeta
    fun getFilesByFolderId(folderId: Int): Flow<List<File>> {
        return repository.getFilesByFolder(folderId)
    }

    // Función para agregar un archivo a una carpeta

    }




