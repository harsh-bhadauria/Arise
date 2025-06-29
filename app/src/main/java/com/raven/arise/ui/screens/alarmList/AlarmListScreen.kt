package com.raven.arise.ui.screens.alarmList

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raven.arise.domain.models.Alarm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListScreen(
    onAddAlarmClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlarmListViewModel = hiltViewModel<AlarmListViewModel>(),
) {
    val alarms by viewModel.alarms.collectAsState(initial = emptyList<Alarm>())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Alarms") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAlarmClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Alarm")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    Button(
                        onClick = { viewModel.instantAlarm() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) { Text("INSTANT ALARM") }
                }

                items(alarms) { alarm ->
                    AlarmItem(alarm = alarm, onDelete = {
                        viewModel.deleteAlarm(alarm)
                    })
                }

            }
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun AlarmItem(
    alarm: Alarm,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = alarm.title)
                Text(text = String.format("%02d:%02d", alarm.hour, alarm.minute))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = "Delete Alarm")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AlarmListScreenPreview() {
    val mockAlarms = listOf(
        Alarm(id = 1, title = "Morning Alarm", hour = 7, minute = 30),
        Alarm(id = 2, title = "Workout Reminder", hour = 9, minute = 0),
        Alarm(id = 3, title = "Study Session", hour = 14, minute = 15),
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Alarms") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = "Add Alarm")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { Text("INSTANT ALARM") }
            }

            items(mockAlarms) { alarm ->
                AlarmItem(alarm = alarm, onDelete = {})
            }
        }
    }
}

