package com.example.gestor_de_tareas.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders")
    fun getAllFolders(): Flow<List<Folder>>

    @Insert
    suspend fun insertFolder(folder: Folder): Long

    @Update
    suspend fun updateFolder(folder: Folder)

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("DELETE FROM folders")
   suspend fun deleteAllFolders()
}

