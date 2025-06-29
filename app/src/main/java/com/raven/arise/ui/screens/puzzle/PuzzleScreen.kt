package com.raven.arise.ui.screens.puzzle

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raven.arise.R
import com.raven.arise.ui.util.pauseAlarmService
import com.raven.arise.ui.util.resumeAlarmService
import com.raven.arise.ui.util.stopAlarmService

@Composable
fun PuzzleScreen(
    onTimeout: () -> Unit,
    onAllPuzzlesSolved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PuzzleViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val progress = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        viewModel.start()
        pauseAlarmService(context)
    }

    LaunchedEffect(uiState.currentPuzzleIndex) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )

        progress.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = PuzzleViewModel.TIMER_DURATION_MS.toInt() - 1000,
                easing = LinearEasing
            )
        )

        if (!uiState.allSolved) {
            resumeAlarmService(context)
            viewModel.updateAnswer("")
            onTimeout()
        }
    }


    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ice_bear),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        )

        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Scaffold(
                containerColor = Color.Transparent
            ) { padding ->
                PuzzleContent(
                    uiState = uiState,
                    progress = progress,
                    onNumberInput = { viewModel.updateAnswer(uiState.answer + it) },
                    onDelete = {
                        if (uiState.answer.isNotEmpty()) {
                            viewModel.updateAnswer(uiState.answer.dropLast(1))
                        }
                    },
                    onSubmit = {
                        val solved = viewModel.submitAnswer(uiState.answer)
                        viewModel.updateAnswer("")
                        if (solved) {
                            stopAlarmService(context)
                            onAllPuzzlesSolved()
                        }
                    },
                    paddingValues = padding
                )
            }
        }

    }


    BackHandler {
        resumeAlarmService(context)
        viewModel.updateAnswer("")
        onTimeout()
    }
}

@Composable
fun PuzzleContent(
    uiState: PuzzleUiState,
    progress: Animatable<Float, *>,
    onNumberInput: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = getProgressColor(progress.value),
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Puzzle ${uiState.currentPuzzleIndex + 1}/3",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            uiState.currentPuzzle.question,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = uiState.answer,
                style = MaterialTheme.typography.displayMedium
            )
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .weight(1f)
        )

        NumberPad(
            onNumberClick = {
                if (it == "OK") onSubmit() else onNumberInput(it)
            },
            onDelete = onDelete
        )
    }
}


@Composable
fun NumberPad(onNumberClick: (String) -> Unit, onDelete: () -> Unit) {
    val buttons = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("Del", "0", "OK")
    )

    Column {
        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "Del" -> onDelete()
                                "OK" -> onNumberClick("OK")
                                else -> onNumberClick(label)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        when (label) {
                            "Del" -> Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Delete",
                                modifier = Modifier.fillMaxSize(0.6f)
                            )

                            "OK" -> Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "OK",
                                modifier = Modifier.fillMaxSize(0.6f)
                            )

                            else -> Text(label, fontSize = 28.sp)
                        }
                    }
                }
            }
        }
    }
}

fun getProgressColor(progress: Float): Color {
    return when {
        progress > 0.5f -> {
            val t = (1f - progress) * 2f
            lerp(Color.Green, Color.Yellow, t)
        }

        else -> {
            val t = (0.5f - progress) * 2f
            lerp(Color.Yellow, Color.Red, t)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PuzzleScreenPreview() {
    val uiState = PuzzleUiState(
        currentPuzzleIndex = 0,
        currentPuzzle = MathPuzzle("55 + 33", "88"),
        answer = "88",
        allSolved = false
    )
    val progress = remember { Animatable(1f) }

    PuzzleContent(
        uiState = uiState,
        progress = progress,
        onNumberInput = {},
        onDelete = {},
        onSubmit = {},
        paddingValues = PaddingValues(16.dp)
    )
}