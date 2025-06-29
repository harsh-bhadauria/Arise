package com.raven.arise.data.local.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raven.arise.domain.models.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}