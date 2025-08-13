package com.example.dayvee.utils

import android.content.Context
import com.example.dayvee.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DurationHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun format(duration: Duration?): String {
        if (duration == null) return context.getString(R.string.duration_zero)

        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        return buildString {
            if (hours > 0) append(context.getString(R.string.hours_format, hours)).append(" ")
            append(context.getString(R.string.minutes_format, minutes))
        }
    }
}