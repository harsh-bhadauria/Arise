package com.raven.arise.domain.repositories

import com.raven.arise.domain.models.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun saveAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun updateAlarm(alarm: Alarm)
}