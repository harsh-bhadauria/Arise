package com.raven.arise.domain.usecases

import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.repositories.AlarmRepository

class DeleteAlarmUseCase(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) = repository.deleteAlarm(alarm)
}