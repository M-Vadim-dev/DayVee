package com.example.dayvee.domain

sealed class TaskValidationError {
    object StartTimeInPast : TaskValidationError()
    object EndTimeBeforeStart : TaskValidationError()
    object InvalidStartTimeFormat : TaskValidationError()
    object InvalidEndTimeFormat : TaskValidationError()
}