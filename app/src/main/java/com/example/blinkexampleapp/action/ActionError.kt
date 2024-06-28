package com.example.blinkexampleapp.action
import kotlinx.serialization.Serializable

@Serializable
data class ActionError(
    /** Non-fatal error message to be displayed to the user */
    val message: String
)
