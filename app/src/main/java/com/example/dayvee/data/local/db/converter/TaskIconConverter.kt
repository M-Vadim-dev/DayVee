package com.example.dayvee.data.local.db.converter

import androidx.room.TypeConverter
import com.example.dayvee.domain.model.TaskIcon

class TaskIconConverter {

    @TypeConverter
    fun fromTaskIcon(icon: TaskIcon?): String = when (icon) {
        is TaskIcon.Resource -> "${TaskIcon.TYPE_RESOURCE}:${icon.resId}"
        is TaskIcon.Custom -> "${TaskIcon.TYPE_CUSTOM}:${icon.uri}"
        TaskIcon.Default, null -> TaskIcon.TYPE_DEFAULT
    }

    @TypeConverter
    fun toTaskIcon(value: String?): TaskIcon = when {
        value == null -> TaskIcon.Default
        value.startsWith("${TaskIcon.TYPE_RESOURCE}:") ->
            TaskIcon.Resource(value.removePrefix("${TaskIcon.TYPE_RESOURCE}:").toInt())

        value.startsWith("${TaskIcon.TYPE_CUSTOM}:") ->
            TaskIcon.Custom(value.removePrefix("${TaskIcon.TYPE_CUSTOM}:"))

        value == TaskIcon.TYPE_DEFAULT -> TaskIcon.Default
        else -> TaskIcon.Default
    }
}
