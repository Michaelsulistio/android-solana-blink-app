package com.example.blinkexampleapp.action

import kotlinx.serialization.Serializable
@Serializable
data class ActionGetResponse(
    /** url of some descriptive image for the action */
    val icon: String,
    /** title of the action */
    val title: String,
    /** brief description of the action */
    val description: String,
    /** text to be rendered on the action button */
    val label: String,
    /** optional state for disabling the action button(s) */
    val disabled: Boolean? = null,
    /** optional list of related Actions */
    val links: ActionLinks? = null,
    /** optional (non-fatal) error message */
    val error: ActionError? = null
)
@Serializable

data class ActionLinks(
    val actions: List<LinkedAction>
)