package com.example.blinkexampleapp.action
data class ActionGetRequest(
    val url: String,
    val properties: Map<String, String> = mapOf("Accept-Encoding" to "application/json"),
    val method: String = "GET",
    val body: String? = null
)
