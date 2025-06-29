package com.raven.arise.ui.screens.addAlarm

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.usecases.AlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class AddAlarmViewModel @Inject constructor(
    private val useCases: AlarmUseCases
) : ViewModel() {

    var timePickerState by mutableStateOf(TimePickerState(8, 0, false))
        private set

    fun createAlarm(): Alarm {
        return Alarm(
            hour = timePickerState.hour,
            minute = timePickerState.minute,
            title = "Alarm at ${timePickerState.hour}:${timePickerState.minute}"
        )
    }

    fun makeToast(alarm: Alarm, context: Context) {
        Toast.makeText(context, "Alarm set for ${alarm.hour}:${alarm.minute}", Toast.LENGTH_SHORT)
            .show()
    }

    fun saveAlarm(alarm: Alarm) {
        viewModelScope.launch {
            useCases.saveAlarmUseCase(alarm)
        }
    }

}