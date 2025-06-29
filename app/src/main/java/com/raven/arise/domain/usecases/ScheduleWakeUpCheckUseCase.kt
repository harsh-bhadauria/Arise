package com.raven.arise.domain.usecases

import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.models.AlarmScheduler
import com.raven.arise.domain.repositories.AlarmRepository

class ScheduleWakeUpCheckUseCase(
    private val scheduler: AlarmScheduler
) {
    operator fun invoke() = scheduler.scheduleWakeUpCheck()
}