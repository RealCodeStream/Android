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

   // @Query("SELECT * FROM files WHERE startDate <= @date AND endDate >= :date")
    //fun getFilesForDate(date: Long): Flow<List<File>>
   //@Query("SELECT * FROM files WHERE date(startDate / 1000, 'unixepoch') <= date(:date / 1000, 'unixepoch') AND date(endDate / 1000, 'unixepoch') >= date(:date / 1000, 'unixepoch')")
   //fun getFilesForDate(date: Long): Flow<List<File>>
   // @Query("SELECT * FROM files WHERE date(:date / 1000, 'unixepoch') BETWEEN date(startDate / 1000, 'unixepoch') AND date(endDate / 1000, 'unixepoch')")
   @Query("SELECT * FROM files WHERE date(startDate / 1000, 'unixepoch', 'localtime') <= date(:date / 1000, 'unixepoch', 'localtime') AND date(endDate / 1000, 'unixepoch', 'localtime') >= date(:date / 1000, 'unixepoch', 'localtime')")
    fun getFilesForDate(date: Long): Flow<List<File>>

    @Query("DELETE FROM files")
   suspend fun deleteAllFiles()




}