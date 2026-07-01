package com.raven.arise.ui.screens.addAlarm

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.models.TaskType
import com.raven.arise.domain.usecases.AlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class AddAlarmViewModel @Inject constructor(
    private val useCases: AlarmUseCases
) : ViewModel() {

    var alarmId by mutableStateOf<Int?>(null)
        private set
    var timePickerState by mutableStateOf(
        TimePickerState(
            initialHour = java.util.Calendar.getInstance().let {
                val h = it.get(java.util.Calendar.HOUR_OF_DAY)
                val m = it.get(java.util.Calendar.MINUTE)
                if (m >= 30) (h + 1) % 24 else h
            },
            initialMinute = 0,
            is24Hour = false
        )
    )
        private set
    var title by mutableStateOf("")
    var repeatDays = mutableStateListOf<Int>()
    var taskType by mutableStateOf(TaskType.NONE)
    var wakeUpCheckEnabled by mutableStateOf(true)

    fun loadAlarm(id: Int?) {
        if (id == null) return
        alarmId = id
        viewModelScope.launch {
            val alarm = useCases.getAlarmByIdUseCase(id)
            alarm?.let {
                timePickerState = TimePickerState(it.hour, it.minute, false)
                title = it.title
                repeatDays.clear()
                repeatDays.addAll(it.repeatDays)
                taskType = it.taskType
                wakeUpCheckEnabled = it.wakeUpCheckEnabled
            }
        }
    }

    fun toggleRepeatDay(day: Int) {
        if (repeatDays.contains(day)) {
            repeatDays.remove(day)
        } else {
            repeatDays.add(day)
        }
    }

    fun saveAlarm(context: Context, onSaved: () -> Unit) {
        val finalTitle = title.trim()
        val alarm = Alarm(
            id = alarmId ?: 0,
            hour = timePickerState.hour,
            minute = timePickerState.minute,
            title = finalTitle,
            description = "",
            isEnabled = true,
            repeatDays = repeatDays.toList(),
            taskType = taskType,
            wakeUpCheckEnabled = wakeUpCheckEnabled
        )

        viewModelScope.launch {
            useCases.saveAlarmUseCase(alarm)
            val msg = getTimeDifferenceMessage(alarm)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            onSaved()
        }
    }

    private fun getTimeDifferenceMessage(alarm: Alarm): String {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, alarm.hour)
            set(java.util.Calendar.MINUTE, alarm.minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)

            val now = java.util.Calendar.getInstance()
            if (alarm.repeatDays.isNotEmpty()) {
                var daysToAdd = 7
                for (repeatDay in alarm.repeatDays) {
                    val targetCalendarDay = repeatDay + 1
                    val currentCalendarDay = get(java.util.Calendar.DAY_OF_WEEK)
                    
                    var diff = targetCalendarDay - currentCalendarDay
                    if (diff < 0 || (diff == 0 && before(now))) {
                        diff += 7
                    }
                    if (diff < daysToAdd) {
                        daysToAdd = diff
                    }
                }
                add(java.util.Calendar.DATE, daysToAdd)
            } else {
                if (before(now)) {
                    add(java.util.Calendar.DATE, 1)
                }
            }
        }

        val diffMillis = calendar.timeInMillis - System.currentTimeMillis()
        val diffMinutes = (diffMillis / (1000 * 60)) % 60
        val diffHours = (diffMillis / (1000 * 60 * 60)) % 24
        val diffDays = diffMillis / (1000 * 60 * 60 * 24)

        return buildString {
            append("Alarm set for ")
            if (diffDays > 0) append("$diffDays days, ")
            if (diffHours > 0) append("$diffHours hours, ")
            if (diffDays == 0L && diffHours == 0L && diffMinutes == 0L) {
                append("less than a minute from now")
            } else {
                append("$diffMinutes minutes from now")
            }
        }
    }
}