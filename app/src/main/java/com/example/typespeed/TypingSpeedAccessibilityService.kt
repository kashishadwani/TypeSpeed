package com.example.typespeed

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class TypingSpeedAccessibilityService : AccessibilityService(){
    private var startTime: Long = 0
    private var wordCount: Int = 0

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if(event?.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED){
            val text = event.text?.toString() ?: return
            val currentTime = System.currentTimeMillis()

            if(startTime == 0L){
                startTime = currentTime
            }

            val elapsedMinutes = (currentTime - startTime) / 60000.0
            wordCount = text.split("\\s+".toRegex()).size

            if(elapsedMinutes > 0){
                val wpm = wordCount / elapsedMinutes
                FloatingIconService.updateWPM(wpm.toInt())
            }
        }
    }

    override fun onInterrupt(){
        startTime = 0
        wordCount = 0
    }
}