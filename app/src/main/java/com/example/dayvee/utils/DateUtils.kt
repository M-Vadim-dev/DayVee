package com.example.dayvee.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateUtils {

    fun formatFullDate(date: LocalDate, locale: Locale = Locale.getDefault()): String =
        date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", locale))
            .replaceFirstChar { it.uppercaseChar() }

    fun formatShortDate(date: LocalDate, locale: Locale = Locale.getDefault()): String =
        date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", locale))

    fun formatTime(timeMillis: Long, locale: Locale = Locale.getDefault()): String =
        SimpleDateFormat("HH:mm", locale).format(Date(timeMillis))

    fun formatTimeWithAmPm(timeMillis: Long, locale: Locale = Locale.getDefault()): String =
        SimpleDateFormat("hh:mm a", locale).format(Date(timeMillis))

    fun formatTimeWithSeconds(timeMillis: Long, locale: Locale = Locale.getDefault()): String =
        SimpleDateFormat("HH:mm:ss", locale).format(Date(timeMillis))

}