package com.example.dayvee.workers

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dayvee.MainActivity
import com.example.dayvee.R
import com.example.dayvee.data.repository.TaskRepositoryImpl
import com.example.dayvee.data.repository.UserRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val parameters: WorkerParameters,
    private val userRepository: UserRepositoryImpl,
    private val taskRepository: TaskRepositoryImpl,
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result = runCatching {
        val activeUser = userRepository.getActiveUser() ?: return Result.failure()

        val today = LocalDate.now()
        val tasks = taskRepository.getTasksForDateOnce(activeUser.uid, today)

        val unfinishedTasksCount = tasks.count { !it.isDone }
        if (unfinishedTasksCount > 0) {
            val title = context.resources.getString(R.string.notification_title)
            val message = context.resources.getQuantityString(
                R.plurals.tasks_for_today,
                unfinishedTasksCount,
                unfinishedTasksCount
            )
            val notification = createNotification(title, message)
            val notificationId = (System.currentTimeMillis() and 0xfffffff).toInt()
            sendNotification(notificationId, notification)
        }
        Result.success()
    }.getOrElse {
        Result.failure()
    }

    private fun createNotification(title: String, message: String): Notification {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_schedule)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setLights(LIGHT_COLOR, LIGHT_ON_MS, LIGHT_OFF_MS)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(notificationId: Int, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    companion object {
        private const val LIGHT_COLOR = Color.RED
        private const val LIGHT_ON_MS = 500
        private const val LIGHT_OFF_MS = 500

        private const val WORK_NAME = "TaskReminderWorker"
        private const val NOTIFICATION_CHANNEL_ID = "notification_channel"
        private const val CHANNEL_NAME = "DayVee Notification"

        fun getWorkName() = WORK_NAME

        fun makeRequest() = androidx.work.PeriodicWorkRequestBuilder<TaskReminderWorker>(
            24,
            java.util.concurrent.TimeUnit.HOURS
        )
            .setInitialDelay(calculateInitialDelay(), java.util.concurrent.TimeUnit.MILLISECONDS)
            .build()

        private fun calculateInitialDelay(): Long {
            val now = java.time.LocalDateTime.now()
            val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
            return java.time.Duration.between(now, nextMidnight).toMillis()
        }
    }
}
