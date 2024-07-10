package com.example.gestor_de_tareas.models

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "task_manager.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFileDao(db: AppDatabase): FileDao = db.fileDao()

    @Provides
    fun provideFolderDao(db: AppDatabase): FolderDao = db.folderDao()

    @Provides
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()


    @Provides
    fun provideTaskRepository(
        fileDao: FileDao,
        folderDao: FolderDao,
        notificationDao: NotificationDao
    ): TaskRepository {
        return TaskRepository(fileDao, folderDao, notificationDao)
    }
}
