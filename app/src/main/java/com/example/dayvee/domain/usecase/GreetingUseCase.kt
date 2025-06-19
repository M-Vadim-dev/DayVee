package com.example.dayvee.domain.usecase

import android.content.Context
import com.example.dayvee.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import javax.inject.Inject

class GreetingUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getGreeting(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 5..11 -> context.getString(R.string.greeting_morning)
            in 12..16 -> context.getString(R.string.greeting_afternoon)
            in 17..20 -> context.getString(R.string.greeting_evening)
            else -> context.getString(R.string.greeting_night)
        }
    }
}