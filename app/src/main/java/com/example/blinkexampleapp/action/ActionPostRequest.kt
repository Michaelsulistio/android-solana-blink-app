package com.example.blinkexampleapp.action
data class ActionPostRequest(
    val url: String,
    val properties: Map<String, String> = mapOf("Accept-Encoding" to "application/json"),
    val method: String = "POST",
    val body: String? = null
)
