package com.example.typespeed

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp

class FloatingIconService : Service(){

    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var composeView: ComposeView


    companion object{
        private var wpmState = mutableStateOf(0)
        fun updateWPM(newWPM: Int){
            wpmState.value = newWPM
        }
    }
    override fun onCreate(){
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 0
        layoutParams.y = 100

        composeView = ComposeView(this).apply {
            setContent {
                FloatingIconUI(wpm = wpmState.value)
            }

            setOnTouchListener{ _, event ->
                when (event.action){
                    MotionEvent.ACTION_DOWN -> {
                        // Record initial touch coordinates
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        this@FloatingIconService.layoutParams.x = event.rawX.toInt() - (width / 2)
                        this@FloatingIconService.layoutParams.y = event.rawY.toInt() - (height / 2)
                        windowManager.updateViewLayout(this, layoutParams)
                        true
                    }
                    else -> false
                }
            }

        }

        windowManager.addView(composeView, layoutParams)

    }
    override fun onDestroy(){
        super.onDestroy()
        windowManager.removeView(composeView)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}

@Composable
fun FloatingIconUI(wpm: Int){
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black.copy(alpha = 0.7f), CircleShape)
    ) {
        Text(
            text = "$wpm WPM",
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
    }
}