package com.raven.arise.data.local

import androidx.room.TypeConverter
import com.raven.arise.domain.models.TaskType

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Int>): String = list.joinToString(",")

    @TypeConverter
    fun toIntList(data: String): List<Int> =
        if (data.isEmpty()) emptyList() else data.split(",").map { it.toInt() }

    @TypeConverter
    fun fromTaskType(type: TaskType): String = type.name

    @TypeConverter
    fun toTaskType(name: String): TaskType = TaskType.valueOf(name)
}