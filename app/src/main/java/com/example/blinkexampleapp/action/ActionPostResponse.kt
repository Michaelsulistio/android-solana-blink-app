package com.example.blinkexampleapp.action

data class ActionPostResponse(
    /** base64-encoded transaction */
    val transaction: String,
    /** optional message, can be used to e.g. describe the nature of the transaction */
    val message: String? = null
)