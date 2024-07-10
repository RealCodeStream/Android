package com.example.gestor_de_tareas.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM files WHERE folderId = :folderId")
    fun getFilesByFolder(folderId: Int): Flow<List<File>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: File)

    @Update
    suspend fun updateFile(file: File)

    @Delete
    suspend fun deleteFile(file: File)
}
