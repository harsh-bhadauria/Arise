package com.raven.arise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.raven.arise.ui.screens.addAlarm.AddAlarmScreen
import com.raven.arise.ui.screens.alarmList.AlarmListScreen
import com.raven.arise.ui.screens.alarmRing.AlarmRingScreen
import com.raven.arise.ui.screens.puzzle.PuzzleScreen
import com.raven.arise.ui.screens.wakeUpCheck.WakeUpCheckScreen

@Composable
fun AppNavDisplay(initialScreen: String? = null, startTime: Long) {
    val initialScreen = when (initialScreen) {
        "alarmRing" -> Screen.AlarmRing
        "wakeUpCheck" -> Screen.WakeUpCheck
        else -> Screen.AlarmList
    }
    val backStack = remember { mutableStateListOf<Screen>(initialScreen) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.AlarmList> {
                AlarmListScreen(
                    onAddAlarmClick = {
                        backStack.add(Screen.AddAlarm)
                    }
                )
            }
            entry<Screen.AddAlarm> {
                AddAlarmScreen(
                    onAlarmSaved = {
                        backStack.removeLastOrNull()
                    },
                    onCancelClicked = { backStack.removeLastOrNull() }
                )
            }

            entry<Screen.AlarmRing> {
                AlarmRingScreen(
                    label = "raven0us",
                    onDismiss = {
                        backStack.add(Screen.Puzzle)
                    },
                    onSnooze = {})
            }

            entry<Screen.Puzzle> {
                PuzzleScreen(
                    onAllPuzzlesSolved = {
                        backStack.clear()
                        backStack.add(Screen.AlarmList)
                    },
                    onTimeout = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<Screen.WakeUpCheck> {
                WakeUpCheckScreen(
                    onDismissClick = {
                        backStack.clear()
                        backStack.add(Screen.AlarmList)
                    },
                    startTime = startTime
                )
            }
        }
    )
}

