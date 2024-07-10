package com.example.gestor_de_tareas.models

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val fileDao: FileDao,
    private val folderDao: FolderDao,
    private val notificationDao: NotificationDao
) {
    fun getAllFolders(): Flow<List<Folder>> = folderDao.getAllFolders()

    suspend fun insertFolder(folder: Folder) = folderDao.insertFolder(folder)

    suspend fun deleteFolder(folder: Folder) = folderDao.deleteFolder(folder)

    fun getFilesByFolder(folderId: Int): Flow<List<File>> = fileDao.getFilesByFolder(folderId)

    suspend fun insertFile(file: File) = fileDao.insertFile(file)

    suspend fun updateFile(file: File) = fileDao.updateFile(file)

    suspend fun deleteFile(file: File) = fileDao.deleteFile(file)

    fun getAllNotifications(): Flow<List<Notification>> = notificationDao.getAllNotifications()

    suspend fun insertNotification(notification: Notification) = notificationDao.insertNotification(notification)

    suspend fun updateNotification(notification: Notification) = notificationDao.updateNotification(notification)

    suspend fun deleteNotification(notification: Notification) = notificationDao.deleteNotification(notification)
}
