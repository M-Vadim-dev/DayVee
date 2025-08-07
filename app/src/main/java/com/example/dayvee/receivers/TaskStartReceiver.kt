package com.example.dayvee.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dayvee.managers.TaskNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskStartReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskNotificationManager: TaskNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1)
        if (taskId == -1L) return

        val taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE) ?: DEFAULT_TASK_TITLE

        taskNotificationManager.notifyTaskStart(taskId, taskTitle)
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        private const val DEFAULT_TASK_TITLE = "Task started"
    }
}