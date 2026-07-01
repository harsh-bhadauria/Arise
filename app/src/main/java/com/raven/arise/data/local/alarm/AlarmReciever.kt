package com.raven.arise.data.local.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.raven.arise.domain.usecases.AlarmUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmUseCases: AlarmUseCases

    override fun onReceive(context: Context, intent: Intent) {
        val label = intent.getStringExtra("label") ?: "Alarm!"
        val id = intent.getIntExtra("id", -1)

        Log.d("AlarmReceiver", "Alarm received!")

        if (id != -1) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val alarm = alarmUseCases.getAlarmByIdUseCase(id)
                    if (alarm != null && alarm.repeatDays.isNotEmpty()) {
                        // Re-saving the alarm will trigger the scheduler to calculate the next date
                        alarmUseCases.saveAlarmUseCase(alarm)
                    } else if (alarm != null && alarm.repeatDays.isEmpty()) {
                        // Disable one-time alarm
                        alarmUseCases.saveAlarmUseCase(alarm.copy(isEnabled = false))
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("label", label)
        }

        ContextCompat.startForegroundService(context, serviceIntent)
    }
}