/*
 * Copyright (c) 2022 Solana Mobile Inc.
 */

package com.example.blinkexampleapp

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkexampleapp.ui.theme.BlinkExampleAppTheme
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlinkComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)

        val windowLayoutParams = window.attributes
        windowLayoutParams.gravity = Gravity.BOTTOM
        windowLayoutParams.flags = windowLayoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowLayoutParams

        val sender = ActivityResultSender(this)

        setContent {
            BlinkExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxHeight(0.5F),
                ) {
                    Blink()
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
    }


    @Composable
    fun Blink() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Title",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Button 1")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Button 2")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Button 3")
                }
            }
        }
    }
}