package com.raven.arise.ui.screens.wakeUpCheck

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class WakeUpCheckViewModel @Inject constructor(

) : ViewModel() {

    private val _startTime = mutableStateOf<Long?>(null)
    val startTime: Long? get() = _startTime.value

    private val _remainingTime = mutableIntStateOf(100)
    val remainingTime: State<Int> = _remainingTime

    fun setStartTime(time: Long) {
        _startTime.value = time

        viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val elapsed = ((now - time) / 1000).toInt()
                val remaining = (100 - elapsed).coerceAtLeast(0)
                _remainingTime.intValue = remaining

                if (remaining <= 0) break
                delay(1000)
            }
        }
    }
}

