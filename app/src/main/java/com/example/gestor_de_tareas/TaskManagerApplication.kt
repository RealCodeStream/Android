package com.example.gestor_de_tareas

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gestor_de_tareas.models.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TaskManagerApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
       // NotificationWorker.scheduleDaily(workManager)
       // createNotificationChannel()
        scheduleNotificationWorker()

    }

    private fun scheduleNotificationWorker() {
        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "notificationWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            notificationWorkRequest
        )
    }
    /*private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }*/
   /* private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                setShowBadge(true)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Timber.d("Notification channel created with high importance")
        }
    }

    companion object {
        const val CHANNEL_ID = "event_notifications"
    }*/

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
