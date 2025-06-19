package com.example.dayvee.utils

import java.time.LocalDate
import java.time.ZoneId

private const val INVALID_HOUR_MESSAGE = "Hour must be a number between 0 and 23"
private const val INVALID_MINUTE_MESSAGE = "Minute must be a number between 0 and 59"

object TimeUtils {

    /**
     * Преобразует дату и строковые значения часов и минут в миллисекунды.
     *
     * @throws IllegalArgumentException если строка не является числом или выходит за пределы допустимого диапазона.
     */
    fun convertToMillis(date: LocalDate, hourStr: String, minuteStr: String): Long {
        val hour = hourStr.toIntOrNull()?.takeIf { it in 0..23 }
            ?: throw IllegalArgumentException(INVALID_HOUR_MESSAGE)

        val minute = minuteStr.toIntOrNull()?.takeIf { it in 0..59 }
            ?: throw IllegalArgumentException(INVALID_MINUTE_MESSAGE)

        val dateTime = date.atTime(hour, minute)
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
