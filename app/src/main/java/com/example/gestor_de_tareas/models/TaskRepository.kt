package com.example.gestor_de_tareas.models

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val folderDao: FolderDao,
    private val fileDao: FileDao,
    private val notificationDao: NotificationDao,
    private val calendarEventDao: CalendarEventDao,
    private val dataStore: DataStore<Preferences>
) {
    fun getAllFolders() = folderDao.getAllFolders()
    suspend fun insertFolder(folder: Folder) = folderDao.insertFolder(folder)
    suspend fun updateFolder(folder: Folder) = folderDao.updateFolder(folder)
    suspend fun deleteFolder(folder: Folder) = folderDao.deleteFolder(folder)

    fun getFilesForFolder(folderId: Int) = fileDao.getFilesForFolder(folderId)
    fun getFileById(fileId: Int) = fileDao.getFileById(fileId)
    suspend fun insertFile(file: File) = fileDao.insertFile(file)
    suspend fun updateFile(file: File) = fileDao.updateFile(file)
    suspend fun deleteFile(file: File) = fileDao.deleteFile(file)

    fun getActiveNotifications() = notificationDao.getActiveNotifications()
    suspend fun markNotificationAsRead(notificationId: Int) {
        val notification = notificationDao.getNotificationById(notificationId)
        notification?.let {
            notificationDao.updateNotification(it.copy(isRead = true))
        }
    }
    suspend fun insertNotification(notification: Notification) = notificationDao.insertNotification(notification)
    suspend fun updateNotification(notification: Notification) = notificationDao.updateNotification(notification)
    suspend fun deleteNotification(notificationId: Int) = notificationDao.deleteNotification(notificationId)

    suspend fun createOrUpdateEventNotification(event: CalendarEvent) {
        val currentTime = System.currentTimeMillis()
        val existingNotification = notificationDao.getNextNotificationForEvent(event.id, currentTime)

        if (existingNotification == null) {
            val notification = Notification(
                eventId = event.id,
                message = "Upcoming event: ${event.title}",
                createdAt = currentTime,
                dueDate = event.startDate
            )
            notificationDao.insertNotification(notification)
        } else {
            val updatedNotification = existingNotification.copy(
                message = "Upcoming event: ${event.title}",
                dueDate = event.startDate
            )
            notificationDao.updateNotification(updatedNotification)
        }
    }
    suspend fun createOrUpdateFileNotification(file: File) {
        val currentTime = System.currentTimeMillis()
        val existingNotification = notificationDao.getNextNotificationForFile(file.id, currentTime)

        if (existingNotification == null) {
            val notification = Notification(
                fileId = file.id,
                message = "Upcoming task: ${file.title}",
                createdAt = currentTime,
                dueDate = file.endDate
            )
            notificationDao.insertNotification(notification)
        } else {
            val updatedNotification = existingNotification.copy(
                message = "Upcoming task: ${file.title}",
                dueDate = file.endDate
            )
            notificationDao.updateNotification(updatedNotification)
        }
    }

    // Nuevo método para obtener archivos para una fecha específica
    fun getFilesForDate(date: Long): Flow<List<File>> = fileDao.getFilesForDate(date)

    suspend fun clearAllData() {
        folderDao.deleteAllFolders()
        fileDao.deleteAllFiles()
        notificationDao.deleteAllNotifications()
    }
    // Preferencias de usuario
    val darkModeEnabled: Flow<Boolean> = dataStore.getValueFlow(DARK_MODE_KEY, false)
    val notificationsEnabled: Flow<Boolean> = dataStore.getValueFlow(NOTIFICATIONS_ENABLED_KEY, true)

    suspend fun updateDarkModeSettings(enabled: Boolean) {
        dataStore.setValue(DARK_MODE_KEY, enabled)
    }

    suspend fun updateNotificationSettings(enabled: Boolean) {
        dataStore.setValue(NOTIFICATIONS_ENABLED_KEY, enabled)
    }

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
    }

    fun getEventsForDate(date: Long): Flow<List<CalendarEvent>> = calendarEventDao.getEventsForDate(date)
    suspend fun insertEvent(event: CalendarEvent) = calendarEventDao.insertEvent(event)
    suspend fun updateEvent(event: CalendarEvent) = calendarEventDao.updateEvent(event)
    suspend fun deleteEvent(event: CalendarEvent) = calendarEventDao.deleteEvent(event)
    fun getEventById(eventId: Int) = calendarEventDao.getEventById(eventId)

}



