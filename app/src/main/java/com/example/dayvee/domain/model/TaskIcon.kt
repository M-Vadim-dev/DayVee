package com.example.dayvee.domain.model

sealed class TaskIcon {
    data class Resource(val resId: Int) : TaskIcon()
    data class Custom(val uri: String) : TaskIcon()
    object Default : TaskIcon()

    companion object {
        const val TYPE_RESOURCE = "Resource"
        const val TYPE_CUSTOM = "Custom"
        const val TYPE_DEFAULT = "Default"
    }
}
