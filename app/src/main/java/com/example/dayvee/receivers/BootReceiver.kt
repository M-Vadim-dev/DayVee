package com.example.dayvee.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.dayvee.workers.TaskReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            workManager.enqueueUniquePeriodicWork(
                TaskReminderWorker.getWorkName(),
                ExistingPeriodicWorkPolicy.REPLACE,
                TaskReminderWorker.makeRequest()
            )
        }
    }
}
