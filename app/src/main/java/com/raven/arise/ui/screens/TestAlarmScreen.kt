package com.raven.arise.ui.screens

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.models.AlarmScheduler
import java.util.Calendar
import androidx.core.net.toUri

@Composable
fun TestAlarmScreen(
    scheduler: AlarmScheduler,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {Button(onClick = {
        requestExactAlarmPermissionIfNeeded(context)
        val now = Calendar.getInstance()
        now.add(Calendar.MINUTE, 1)

        val alarm = Alarm(
            id = now.timeInMillis.toInt(), // crude unique ID
            hour = now.get(Calendar.HOUR_OF_DAY),
            minute = now.get(Calendar.MINUTE),
            title = "Test Alarm"
        )

        scheduler.schedule(alarm)
        Toast.makeText(context, "Alarm set for ${alarm.hour}:${alarm.minute}", Toast.LENGTH_SHORT).show()

    }) {
        Text("Schedule 1-Minute Test Alarm")
    }  }

}

fun requestExactAlarmPermissionIfNeeded(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = "package:${context.packageName}".toUri()
            context.startActivity(intent)
        }
    }
}
