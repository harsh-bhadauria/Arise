package com.raven.arise.domain.usecases

import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.models.AlarmScheduler
import com.raven.arise.domain.repositories.AlarmRepository

class SaveAlarmUseCase(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.saveAlarm(alarm)
        scheduler.schedule(alarm)
    }
}