package com.example.gestor_de_tareas.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/*@Database(entities = [Folder::class, File::class, Notification::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun fileDao(): FileDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_manager_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}*/
@Database(entities = [Folder::class, File::class, Notification::class, CalendarEvent::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun fileDao(): FileDao
    abstract fun notificationDao(): NotificationDao
    abstract fun calendarEventDao(): CalendarEventDao

   /* companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migración añadida en versión 1.1.0 de la app
        private val MIGRATION_1_2 = object : Migration(1, 2) {
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
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_manager_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }*/
}