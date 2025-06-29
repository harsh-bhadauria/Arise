package com.raven.arise.domain.usecases

import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.repositories.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmsUseCase(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getAlarms()
}