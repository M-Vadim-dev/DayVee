package com.example.dayvee.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.example.dayvee.receivers.TaskStartReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskStartAlarmManager @Inject constructor(
    @param:ApplicationContext private val applicationContext: Context,
) {
    fun scheduleTaskStartAlarm(taskId: Long, taskTitle: String, startTime: ZonedDateTime) {
        val alarmManager = applicationContext.getSystemService(AlarmManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w("TaskStartAlarmManager", "Exact alarm permission not granted")
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                applicationContext.startActivity(intent)
                return
            }
        }

        val triggerAtMillis = startTime.toInstant().toEpochMilli()
        val pendingIntent = createPendingIntent(taskId, taskTitle)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    fun cancelTaskStartAlarm(taskId: Long, taskTitle: String) {
        val alarmManager = applicationContext.getSystemService(AlarmManager::class.java)
        alarmManager.cancel(createPendingIntent(taskId, taskTitle))
    }

    private fun createPendingIntent(taskId: Long, taskTitle: String): PendingIntent {
        val intent = Intent(applicationContext, TaskStartReceiver::class.java).apply {
            putExtra(TaskStartReceiver.EXTRA_TASK_ID, taskId)
            putExtra(TaskStartReceiver.EXTRA_TASK_TITLE, taskTitle)
        }
        return PendingIntent.getBroadcast(
            applicationContext,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

}
