package com.raven.arise.domain.usecases

import com.raven.arise.domain.models.AlarmScheduler

class ScheduleWakeUpCheckUseCase(
    private val scheduler: AlarmScheduler
) {
    operator fun invoke() = scheduler.scheduleWakeUpCheck()
}