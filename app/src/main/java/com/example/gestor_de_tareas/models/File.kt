package com.example.gestor_de_tareas.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "files",
    foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["folderId"])]
)
data class File(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "folderId") val folderId: Int,
    val title: String,
    val content: String,
    val attachmentPath: String?,
    val date: String
)
