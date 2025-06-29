package com.raven.arise.ui.screens.addAlarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmScreen(
    onAlarmSaved: () -> Unit,
    onCancelClicked: () -> Unit,
    viewModel: AddAlarmViewModel = hiltViewModel<AddAlarmViewModel>()
) {
    val context = LocalContext.current
    val timePickerState = viewModel.timePickerState

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(state = timePickerState)

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                val alarm = viewModel.createAlarm()
                viewModel.saveAlarm(alarm)
                viewModel.makeToast(alarm, context)
                onAlarmSaved()
            }) {
                Text("Save Alarm")
            }
            Button(onClick = onCancelClicked) {
                Text("Cancel")
            }
        }
    }
}
