package com.raven.arise.data.local.wakeUpCheck

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.raven.arise.MainActivity
import com.raven.arise.R
import com.raven.arise.data.local.alarm.AlarmReceiver

class WakeUpCheckReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("wakeUpCheckReceiver", "wakeUpCheck received!")

        val startTime = System.currentTimeMillis()

        showWakeUpCheckNotification(context, startTime)

        val reRingIntent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            101,
            reRingIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 100 * 1000, // Set to 10 seconds later for testing
            pendingIntent
        )
    }

    private fun showWakeUpCheckNotification(context: Context, startTime: Long) {
        val channelId = "alarm_channel"
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("initialScreen", "wakeUpCheck")
            putExtra("startTime", startTime)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Still awake?")
            .setContentText("Tap to confirm within 100 seconds")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        notificationManager.notify(3, notification)
    }
}

