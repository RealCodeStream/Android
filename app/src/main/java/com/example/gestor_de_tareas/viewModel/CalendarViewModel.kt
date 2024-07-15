package com.example.gestor_de_tareas.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestor_de_tareas.models.CalendarEvent
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.models.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

   /* fun getTasksForDate(date: Long): Flow<List<File>> {
        return repository.getFilesForDate(date)
    }

    fun getEventsForDate(date: Long): Flow<List<CalendarEvent>> {
        return repository.getEventsForDate(date)
    }*/
   @RequiresApi(Build.VERSION_CODES.O)
   fun getTasksForDate(date: Long): Flow<List<File>> {
        val startOfDay = LocalDate.ofEpochDay(date / 86400000).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return repository.getFilesForDate(startOfDay)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsForDate(date: Long): Flow<List<CalendarEvent>> {
        val startOfDay = LocalDate.ofEpochDay(date / 86400000).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return repository.getEventsForDate(startOfDay)
    }

    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            repository.insertEvent(event)
        }
    }
}