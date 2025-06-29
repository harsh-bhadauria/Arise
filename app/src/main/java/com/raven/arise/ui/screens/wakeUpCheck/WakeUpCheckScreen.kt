package com.raven.arise.ui.screens.wakeUpCheck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raven.arise.ui.util.stopReRing

@Composable
fun WakeUpCheckScreen(
    startTime: Long,
    onDismissClick: () -> Unit,
    viewModel: WakeUpCheckViewModel = hiltViewModel(),
) {
    LaunchedEffect(startTime) {
        viewModel.setStartTime(startTime)
    }

    val context = LocalContext.current
    val remainingTime by viewModel.remainingTime

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Are you awake?", style = MaterialTheme.typography.displayMedium)

            Text(
                "Confirm before the timer runs out",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "$remainingTime",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            Button(
                onClick = {
                    stopReRing(context)
                    onDismissClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Yes, I'm awake!",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun WakeUpCheckScreenPreview() {
    WakeUpCheckScreen(
        startTime = System.currentTimeMillis(),
        onDismissClick = {}
    )
}
