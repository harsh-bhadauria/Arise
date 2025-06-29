package com.raven.arise.data.local.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.raven.arise.data.local.alarm.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val label = intent.getStringExtra("label") ?: "Alarm!"

        Log.d("AlarmReceiver", "Alarm received!")

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("label", label)
        }

        ContextCompat.startForegroundService(context, serviceIntent)
    }
}