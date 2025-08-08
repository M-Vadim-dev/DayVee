package com.example.dayvee.domain

sealed class TaskValidationError {
    object StartTimeInPast : TaskValidationError()
    object EndTimeBeforeStart : TaskValidationError()
    object InvalidStartTimeFormat : TaskValidationError()
    object InvalidEndTimeFormat : TaskValidationError()
    object InvalidStartHour : TaskValidationError()
    object InvalidStartMinute : TaskValidationError()
    object InvalidEndHour : TaskValidationError()
    object InvalidEndMinute : TaskValidationError()
}