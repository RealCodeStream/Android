package com.example.gestor_de_tareas.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = File::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CalendarEvent::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["fileId"]),
               Index(value = ["eventId"])
              ]
)
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "fileId") val fileId: Int? = null,
    @ColumnInfo(name = "eventId") val eventId: Int? = null,
    val message: String,
    val createdAt: Long,
    val dueDate: Long,
    val isRead: Boolean = false
)
