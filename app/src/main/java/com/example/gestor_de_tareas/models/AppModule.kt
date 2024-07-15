package com.example.gestor_de_tareas.models

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
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
        )
            //.addMigrations(MIGRATION_1_2)
            .build()
    }

    /*private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `calendar_events` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `title` TEXT NOT NULL,
                    `description` TEXT NOT NULL,
                    `startDate` INTEGER NOT NULL,
                    `endDate` INTEGER NOT NULL
                )
            """)
        }
    }*/


    @Provides
    fun provideFolderDao(database: AppDatabase) = database.folderDao()

    @Provides
    fun provideFileDao(database: AppDatabase) = database.fileDao()

    @Provides
    fun provideNotificationDao(database: AppDatabase) = database.notificationDao()

    @Provides
    fun provideCalendarEventDao(database: AppDatabase) = database.calendarEventDao()

    @Provides
    @Singleton
    fun provideTaskRepository(
        folderDao: FolderDao,
        fileDao: FileDao,
        notificationDao: NotificationDao,
        calendarEventDao: CalendarEventDao,
        dataStore: DataStore<Preferences>
    ) = TaskRepository(folderDao, fileDao, notificationDao, calendarEventDao,dataStore )

    @SuppressLint("ServiceCast")
    @Provides
    @Singleton
    fun provideNotificationService(@ApplicationContext context: Context) =
        NotificationService(context, context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

    @Provides
    @Singleton
    fun provideAttachmentHandler(@ApplicationContext context: Context) = AttachmentHandler(context)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}
