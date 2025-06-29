package com.raven.arise.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val hour: Int = 0,
    val minute: Int = 0,
    val isEnabled: Boolean = true,
    val repeatDays: List<Int> = emptyList(), // Days of the week (0=Sunday, 6=Saturday)
    val taskType: TaskType = TaskType.NONE,  // What task to do to dismiss
    val wakeUpCheckEnabled: Boolean = true
)