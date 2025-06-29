package com.raven.arise.data.repositories

import com.raven.arise.data.local.alarm.AlarmDao
import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.repositories.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val dao: AlarmDao
) : AlarmRepository {
    override fun getAlarms(): Flow<List<Alarm>> = dao.getAlarms()
    override suspend fun saveAlarm(alarm: Alarm) = dao.saveAlarm(alarm)
    override suspend fun deleteAlarm(alarm: Alarm) = dao.deleteAlarm(alarm)
    override suspend fun updateAlarm(alarm: Alarm) {
        TODO("Not yet implemented")
    }
}
