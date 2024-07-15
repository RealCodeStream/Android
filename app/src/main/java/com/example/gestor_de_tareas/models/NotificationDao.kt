package com.example.gestor_de_tareas.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY dueDate ASC")
    fun getActiveNotifications(): Flow<List<Notification>>

    @Insert
    suspend fun insertNotification(notification: Notification): Long

    @Update
    suspend fun updateNotification(notification: Notification)

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: Int)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("SELECT * FROM notifications WHERE eventId = :eventId AND dueDate > :currentTime LIMIT 1")
    suspend fun getNextNotificationForEvent(eventId: Int, currentTime: Long): Notification?

    @Query("SELECT * FROM notifications WHERE fileId = :fileId AND dueDate > :currentTime LIMIT 1")
    suspend fun getNextNotificationForFile(fileId: Int, currentTime: Long): Notification?

    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: Int): Notification?
}