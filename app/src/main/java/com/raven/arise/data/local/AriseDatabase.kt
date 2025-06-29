package com.raven.arise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raven.arise.data.local.alarm.AlarmDao
import com.raven.arise.domain.models.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AriseDatabase: RoomDatabase(){
    abstract fun alarmDao(): AlarmDao
}