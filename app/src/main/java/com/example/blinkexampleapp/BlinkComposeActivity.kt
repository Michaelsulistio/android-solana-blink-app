/*
 * Copyright (c) 2022 Solana Mobile Inc.
 */

package com.example.blinkexampleapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkexampleapp.action.ActionGetResponse
import com.example.blinkexampleapp.blink.Blink
import com.example.blinkexampleapp.networking.KtorHttpDriver
import com.example.blinkexampleapp.ui.theme.BlinkExampleAppTheme
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.networking.HttpNetworkDriver
import com.solana.networking.HttpRequest
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class BlinkComposeActivity : ComponentActivity() {

    val requestClient = HttpClient(Android)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)

        val windowLayoutParams = window.attributes
        windowLayoutParams.gravity = Gravity.BOTTOM
        windowLayoutParams.flags =
            windowLayoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowLayoutParams

        val sender = ActivityResultSender(this)
        val actionUrl = extractActionUrl(this.intent)
        println(actionUrl)

        setContent {
            BlinkExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxHeight(),
                    color = Color.Transparent

                    ) {
                    BlinkBottomSheetDialog(actionUrl)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BlinkBottomSheetDialog(actionUrl: String?) {
        val coroutineScope = rememberCoroutineScope()
        var actionResponse by remember { mutableStateOf<ActionGetResponse?>(null) }

        val activity = this;
        LaunchedEffect(actionUrl) {
            if (actionUrl != null) {
                println("LAUNCHING COROUTINE")
                coroutineScope.launch {
                    println("COROUTINE launched")
                    try {
                        val client = HttpClient()
                        val response: HttpResponse =
                            client.get("https://dial.to/api/helius/stake") {
                                header("Accept", "application/json")
                            }

                        val responseBody = response.bodyAsText()
                        val json = Json { ignoreUnknownKeys = true }
                        actionResponse = json.decodeFromString<ActionGetResponse>(responseBody)
                    } catch (e: Exception) {
                        println("Action GET error occurred: ${e.message}")
                        println(e.stackTrace)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // Dismiss the activity when tapped outside
                            activity.finish()
                        }
                    )
                },
            contentAlignment = Alignment.BottomCenter

        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                // no-op
                            }
                        )
                    }
            ) {
                if (actionResponse != null) {
                    Blink(
                        icon = actionResponse!!.icon,
                        title = actionResponse!!.title,
                        description = actionResponse!!.description,
                        label = actionResponse!!.label,
                        disabled = actionResponse!!.disabled,
                        links = actionResponse!!.links,
                        error = actionResponse!!.error
                    )
                } else {
                    PlaceholderView()
                }
            }
        }
    }
}
@Composable
fun PlaceholderView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No URL provided",
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}

private fun extractActionUrl(intent: Intent): String? {
    val data: Uri? = intent.data
    return data?.toString()?.removePrefix("${data?.scheme}://")
}