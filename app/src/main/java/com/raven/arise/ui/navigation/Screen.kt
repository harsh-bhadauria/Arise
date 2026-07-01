package com.raven.arise.ui.navigation

sealed class Screen{
    data object AlarmList: Screen()
    data class AddAlarm(val alarmId: Int? = null): Screen()
    data object AlarmRing: Screen()
    data object Puzzle: Screen()
    data object WakeUpCheck: Screen()
}
