package com.raven.arise.domain.usecases

data class AlarmUseCases(
    val saveAlarmUseCase: SaveAlarmUseCase,
    val deleteAlarmUseCase: DeleteAlarmUseCase,
    val getAlarmsUseCase: GetAlarmsUseCase,
    val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    val scheduleWakeUpCheckUseCase: ScheduleWakeUpCheckUseCase,
)
