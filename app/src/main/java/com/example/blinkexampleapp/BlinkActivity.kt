/*
 * Copyright (c) 2022 Solana Mobile Inc.
 */

package com.example.blinkexampleapp

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class BlinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
        setContentView(R.layout.activity_blink_bottom_sheet)

        val windowLayoutParams = window.attributes
        windowLayoutParams.gravity = Gravity.BOTTOM
        windowLayoutParams.flags = windowLayoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowLayoutParams
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
    }
}