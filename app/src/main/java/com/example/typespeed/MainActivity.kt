package com.example.typespeed

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.typespeed.ui.theme.TypeSpeedTheme

class MainActivity : ComponentActivity() {
    private var isFloatingServiceRunning = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TypeSpeedTheme {
                TypeSpeedApp(
                isFloatingServiceRunning = isFloatingServiceRunning.value,
                onStartService = {startFloatingIconService()},
                onStopService = {stopFloatingIconService()}
                )
            }
        }
    }
    private fun startFloatingIconService(){
        if(Settings.canDrawOverlays(this)){
            val intent = Intent(this,FloatingIconService::class.java)
            ContextCompat.startForegroundService(this,intent)
            isFloatingServiceRunning.value = true
        }else{
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(intent)
        }
    }
    private fun stopFloatingIconService(){
        val intent = Intent(this, FloatingIconService::class.java)
        stopService(intent)
        isFloatingServiceRunning.value = false

    }
}

@Composable
fun TypeSpeedApp(
    isFloatingServiceRunning: Boolean,
    onStartService: ()-> Unit,
    onStopService: ()-> Unit
){
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text="Typing Speed Service", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if(isFloatingServiceRunning){
            Button(onClick = {onStopService()}) {
                Text("Stop Typing Speed Tracker")
            }
        } else {
            Button(onClick = {onStartService()}) {
                Text("Start Typing Speed Tracker")
            }
        }
    }
}

