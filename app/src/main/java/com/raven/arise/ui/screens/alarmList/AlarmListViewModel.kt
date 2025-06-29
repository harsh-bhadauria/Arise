package com.raven.arise.ui.screens.alarmList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.usecases.AlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class AlarmListViewModel @Inject constructor(
    private val useCases: AlarmUseCases
) : ViewModel() {

    val alarms = useCases.getAlarmsUseCase()

    fun instantAlarm() {
        val now = Calendar.getInstance()
        now.add(Calendar.MINUTE, 1)

        val alarm = Alarm(
            hour = now.get(Calendar.HOUR_OF_DAY),
            minute = now.get(Calendar.MINUTE),
            title = "Test Alarm"
        )
        saveAlarm(alarm)
    }
    fun saveAlarm(alarm: Alarm) {
        viewModelScope.launch {
            useCases.saveAlarmUseCase(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            useCases.deleteAlarmUseCase(alarm)
        }
    }


}
