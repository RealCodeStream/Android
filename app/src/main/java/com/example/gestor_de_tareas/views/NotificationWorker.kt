package com.example.gestor_de_tareas.views

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.media3.common.MediaLibraryInfo.TAG
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.gestor_de_tareas.MainActivity
import com.example.gestor_de_tareas.R
import com.example.gestor_de_tareas.models.CalendarEvent
import com.example.gestor_de_tareas.models.File
import com.example.gestor_de_tareas.models.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val CHANNEL_ID = "event_task_notifications"
    }

    override suspend fun doWork(): Result {
        createNotificationChannel()
        checkAndNotifyUpcomingEvents()
        checkAndNotifyUpcomingTasks()
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event and Task Notifications"
            val descriptionText = "Notifications for upcoming events and tasks"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private suspend fun checkAndNotifyUpcomingEvents() {
        val currentTime = System.currentTimeMillis()
        val events = repository.getEventsForDate(currentTime).first()
        events.forEach { event ->
            if (event.startDate > currentTime && event.startDate - currentTime <= 30 * 60 * 1000) { // 30 minutes
                createEventNotification(event)
            }
        }
    }

    private suspend fun checkAndNotifyUpcomingTasks() {
        val currentTime = System.currentTimeMillis()
        val files = repository.getFilesForDate(currentTime).first()
        files.forEach { file ->
            if (file.endDate > currentTime && file.endDate - currentTime <= 30 * 60 * 1000) { // 30 minutes
                createTaskNotification(file)
            }
        }
    }

    private fun createEventNotification(event: CalendarEvent) {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(appContext, event.id, intent, flag)

        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Upcoming Event: ${event.title}")
            .setContentText("Starting in ${getTimeUntilStart(event.startDate)}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your event '${event.title}' is starting in ${getTimeUntilStart(event.startDate)}. Don't forget to prepare!"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(event.id, builder.build())
        }
        Timber.d("Notification sent for event: ${event.title}")
    }

    private fun createTaskNotification(file: File) {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(appContext, file.id, intent, flag)

        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Upcoming Task: ${file.title}")
            .setContentText("Due in ${getTimeUntilEnd(file.endDate)}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your task '${file.title}' is due in ${getTimeUntilEnd(file.endDate)}. Make sure to complete it on time!"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(file.id, builder.build())
        }
        Timber.d("Notification sent for task: ${file.title}")
    }

    private fun getTimeUntilStart(startDate: Long): String {
        val diff = startDate - System.currentTimeMillis()
        val minutes = diff / (60 * 1000)
        return when {
            minutes > 60 -> "${minutes / 60} hours"
            else -> "$minutes minutes"
        }
    }

    private fun getTimeUntilEnd(endDate: Long): String {
        val diff = endDate - System.currentTimeMillis()
        val minutes = diff / (60 * 1000)
        return when {
            minutes > 60 -> "${minutes / 60} hours"
            else -> "$minutes minutes"
        }
    }
}

/*@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepository,
    private val notificationManager: NotificationManager
) : CoroutineWorker(appContext, workerParams) {

    @OptIn(UnstableApi::class)
    override suspend fun doWork(): Result {
        Log.d(TAG, "NotificationWorker started")
        val currentTime = System.currentTimeMillis()
        val twoHoursFromNow = currentTime + TWO_HOURS_IN_MILLIS

        // Verificar si las notificaciones están habilitadas
        if (!repository.notificationsEnabled.first()) {
            Log.d(TAG, "Notifications are disabled. Exiting.")
            return Result.success()
        }

        // Verificar eventos próximos
        val upcomingEvents = repository.getEventsForDate(twoHoursFromNow).first()
        Log.d(TAG, "Found ${upcomingEvents.size} upcoming events")
        upcomingEvents.forEach { event ->
            if (event.startDate in (currentTime + 1)..twoHoursFromNow) {
                Log.d(TAG, "Showing notification for event: ${event.title}")
                showEventNotification(event)
            }
        }

        // Verificar archivos (tareas) próximos
        val upcomingFiles = repository.getFilesForDate(twoHoursFromNow).first()
        Log.d(TAG, "Found ${upcomingFiles.size} upcoming files/tasks")
        upcomingFiles.forEach { file ->
            if (file.endDate in (currentTime + 1)..twoHoursFromNow) {
                Log.d(TAG, "Showing notification for file/task: ${file.title}")
                showFileNotification(file)
            }
        }
        Log.d(TAG, "NotificationWorker finished")
        return Result.success()
    }

    @OptIn(UnstableApi::class)
    private fun showEventNotification(event: CalendarEvent) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            event.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, AppConstants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // Asegúrate de tener este icono
            .setContentTitle("Evento próximo: ${event.title}")
            .setContentText("Comienza en ${getTimeUntilStart(event.startDate)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Sonido, vibración y luces LED por defecto
            .build()

        notificationManager.notify(event.id, notification)
        Timber.d("Push notification shown for event: ${event.title}")

    }
    /*private fun showEventNotification(event: CalendarEvent) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            event.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // Asegúrate de tener este icono
            .setContentTitle("Próximo evento: ${event.title}")
            .setContentText("Comienza en ${getTimeUntilStart(event.startDate)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(event.id, notification)
        Log.d(TAG, "Event notification shown for: ${event.title}")
    }*/

    @OptIn(UnstableApi::class)
    private fun showFileNotification(file: File) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            file.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, AppConstants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle("Tarea próxima: ${file.title}")
            .setContentText("Vence en ${getTimeUntilEnd(file.endDate)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(file.id, notification)
        Timber.d("Push notification shown for file/task: ${file.title}")
    }
    /*private fun showFileNotification(file: File) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            file.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            //.setSmallIcon(R.drawable.ic_notification) // Asegúrate de tener este icono
            .setContentTitle("Tarea próxima: ${file.title}")
            .setContentText("Vence en ${getTimeUntilEnd(file.endDate)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(file.id, notification)
        Log.d(TAG, "File/Task notification shown for: ${file.title}")
    }*/

    private fun getTimeUntilStart(startDate: Long): String {
        val diff = startDate - System.currentTimeMillis()
        val hours = diff / (60 * 60 * 1000)
        val minutes = (diff % (60 * 60 * 1000)) / (60 * 1000)
        return when {
            hours > 0 -> "$hours horas y $minutes minutos"
            minutes > 0 -> "$minutes minutos"
            else -> "menos de un minuto"
        }
    }

    private fun getTimeUntilEnd(endDate: Long): String {
        val diff = endDate - System.currentTimeMillis()
        val hours = diff / (60 * 60 * 1000)
        val minutes = (diff % (60 * 60 * 1000)) / (60 * 1000)
        return when {
            hours > 0 -> "$hours horas y $minutes minutos"
            minutes > 0 -> "$minutes minutos"
            else -> "menos de un minuto"
        }
    }

    companion object {
        private const val TWO_HOURS_IN_MILLIS = 5 * 60 * 1000 //2 * 60 * 60 * 1000
        const val CHANNEL_ID = "event_notifications"
    }
}
*/

/*@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepository,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val currentTime = System.currentTimeMillis()
        val events = repository.getEventsForDate(currentTime).first()
        val files = repository.getFilesForDate(currentTime).first()

        events.forEach { event ->
            if (event.startDate > currentTime && event.startDate - currentTime <= TWO_HOURS_IN_MILLIS) {
                repository.createOrUpdateEventNotification(event)
                showNotification(event)
            }
        }

        files.forEach { file ->
            if (file.startDate > currentTime && file.endDate - currentTime <= TWO_HOURS_IN_MILLIS) {
                repository.createOrUpdateFileNotification(file)
                showNotification(file)
            }
        }

        return Result.success()
    }


    private fun showNotification(event: CalendarEvent) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            //.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Upcoming Event")
            .setContentText(event.title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(event.id, notification)
    }
    private fun showNotification(file: File) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            //.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Upcoming Task")
            .setContentText(file.title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(file.id, notification)
    }


    companion object {
        private const val TWO_HOURS_IN_MILLIS = 2 * 60 * 60 * 1000
        const val CHANNEL_ID = "event_notifications"


        fun schedule(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val repeatingRequest = PeriodicWorkRequestBuilder<NotificationWorker>(2, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "event_notification_worker",
                ExistingPeriodicWorkPolicy.UPDATE,
                repeatingRequest
            )
        }
    }
}*/