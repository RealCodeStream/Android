package com.example.gestor_de_tareas.models

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.gestor_de_tareas.MainActivity
import com.example.gestor_de_tareas.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManager
) {
    fun showNotification(notification: Notification) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            /*.setSmallIcon(R.drawable.ic_notification)*/
            .setContentTitle(notification.message)
            .setContentText("Due: ${DateUtils.formatDateTime(context, notification.dueDate, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notification.id, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "task_notifications"
    }
}

class AttachmentHandler @Inject constructor(
    private val context: Context
) {
    suspend fun saveAttachment(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "attachments/${System.currentTimeMillis()}_${uri.lastPathSegment}")
            file.parentFile?.mkdirs()
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }
    }

    fun openAttachment(path: String) {
        val file = File(path)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }
}

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
    private val notificationService: NotificationService,
    private val repository: TaskRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val currentTime = System.currentTimeMillis()
        val notifications = repository.getActiveNotifications().first()

        notifications.forEach { notification ->
            if (notification.dueDate <= currentTime) {
                notificationService.showNotification(notification)
                repository.updateNotification(notification.copy(isRead = true))
            }
        }

        return Result.success()
    }

    companion object {
        fun scheduleDaily(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "dailyNotificationCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )
        }
    }
}