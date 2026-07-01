package com.raven.arise.domain.usecases

import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.repositories.AlarmRepository
import javax.inject.Inject

class GetAlarmByIdUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Int): Alarm? {
        return repository.getAlarmById(id)
    }
}
