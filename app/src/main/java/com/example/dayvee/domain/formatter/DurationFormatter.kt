package com.example.dayvee.domain.formatter

import java.time.Duration

interface DurationFormatter {
    fun format(duration: Duration?): String
}