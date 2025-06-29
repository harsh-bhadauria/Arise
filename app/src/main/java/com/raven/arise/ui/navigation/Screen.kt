package com.raven.arise.ui.navigation

sealed class Screen{
    data object AlarmList: Screen()
    data object AddAlarm: Screen()
    data object AlarmRing: Screen()
    data object Puzzle: Screen()
    data object WakeUpCheck: Screen()
}
