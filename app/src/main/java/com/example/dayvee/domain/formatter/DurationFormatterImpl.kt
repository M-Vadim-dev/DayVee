package com.example.dayvee.domain.formatter

import com.example.dayvee.utils.DurationHelper
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DurationFormatterImpl @Inject constructor(
    private val durationHelper: DurationHelper
) : DurationFormatter {
    override fun format(duration: Duration?): String = durationHelper.format(duration)
}