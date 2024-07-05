package com.example.gestor_de_tareas.models

import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val folderDao: FolderDao,
    private val fileDao: FileDao,
    private val notificationDao: NotificationDao
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
    suspend fun insertNotification(notification: Notification) = notificationDao.insertNotification(notification)
    suspend fun updateNotification(notification: Notification) = notificationDao.updateNotification(notification)
    suspend fun deleteNotification(notificationId: Int) = notificationDao.deleteNotification(notificationId)
    // Implementa métodos para interactuar con la base de datos
}