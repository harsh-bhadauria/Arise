package com.raven.arise.domain.models

interface AlarmScheduler {
    fun schedule(alarm: Alarm)

    fun cancel(alarm: Alarm)

    fun scheduleWakeUpCheck()
}