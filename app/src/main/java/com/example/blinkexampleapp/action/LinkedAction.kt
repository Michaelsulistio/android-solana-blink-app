package com.example.blinkexampleapp.action

import kotlinx.serialization.Serializable

@Serializable
data class LinkedAction(
    /** URL endpoint for an action */
    val href: String,
    /** button text rendered to the user */
    val label: String,
    /** Parameter to accept user input within an action */
    val parameters: List<ActionParameter>? = null
)

/** Parameter to accept user input within an action */
@Serializable
data class ActionParameter(
    /** parameter name in url */
    val name: String,
    /** placeholder text for the user input field */
    val label: String? = null,
    /** declare if this field is required (defaults to `false`) */
    val required: Boolean? = false
)
