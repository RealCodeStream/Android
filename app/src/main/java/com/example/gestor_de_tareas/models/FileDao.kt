package com.example.gestor_de_tareas.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM files WHERE folderId = :folderId")
    fun getFilesByFolder(folderId: Int): Flow<List<File>>

    @Query("SELECT * FROM files WHERE id = :fileId")
    fun getFileById(fileId: Int): Flow<File?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: File)

    @Update
    suspend fun updateFile(file: File)

    @Delete
    suspend fun deleteFile(file: File)
}
