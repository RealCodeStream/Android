package com.example.gestor_de_tareas.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM files WHERE folderId = :folderId")
    fun getFilesForFolder(folderId: Int): Flow<List<File>>

    @Query("SELECT * FROM files WHERE id = :fileId")
    fun getFileById(fileId: Int): Flow<File?>

    @Insert
    suspend fun insertFile(file: File): Long

    @Update
    suspend fun updateFile(file: File)

    @Delete
    suspend fun deleteFile(file: File)
}