package com.example.dayvee.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object DateUtils {

    fun formatFullDate(date: LocalDate, locale: Locale = Locale.getDefault()): String =
        date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", locale))
            .replaceFirstChar { it.uppercaseChar() }

    fun formatDate(date: LocalDate, locale: Locale = Locale.getDefault()): String =
        date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", locale))
            .replaceFirstChar { it.uppercaseChar() }

    fun formatShortDate(date: LocalDate, locale: Locale = Locale.getDefault()): String =
        date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", locale))

    fun formatTime(
        timeMillis: Long,
        locale: Locale = Locale.getDefault(),
        useAmPm: Boolean? = null,
    ): String {
        val time = Instant.ofEpochMilli(timeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        val formatter = when (useAmPm) {
            true -> DateTimeFormatter.ofPattern("hh:mm a", locale)
            false -> DateTimeFormatter.ofPattern("HH:mm", locale)
            null -> DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)
        }

        return time.format(formatter)
    }

}