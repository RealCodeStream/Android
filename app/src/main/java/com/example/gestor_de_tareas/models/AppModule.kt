package com.example.gestor_de_tareas.models

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_manager_database"
        ).build()
    }

    @Provides
    fun provideFolderDao(database: AppDatabase) = database.folderDao()

    @Provides
    fun provideFileDao(database: AppDatabase) = database.fileDao()

    @Provides
    fun provideNotificationDao(database: AppDatabase) = database.notificationDao()

    @Provides
    @Singleton
    fun provideTaskRepository(
        folderDao: FolderDao,
        fileDao: FileDao,
        notificationDao: NotificationDao
    ) = TaskRepository(folderDao, fileDao, notificationDao)
}