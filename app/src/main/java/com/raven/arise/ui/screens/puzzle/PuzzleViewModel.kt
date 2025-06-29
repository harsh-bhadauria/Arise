package com.raven.arise.ui.screens.puzzle

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import com.raven.arise.domain.usecases.AlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PuzzleViewModel @Inject constructor(
    private val useCases: AlarmUseCases
) : ViewModel() {

    companion object {
        const val TIMER_DURATION_MS = 15_000L
    }

    private val _uiState = MutableStateFlow(PuzzleUiState())
    val uiState: StateFlow<PuzzleUiState> = _uiState

    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer

    var targetProgress = mutableFloatStateOf(1f) // Start at 1f

    fun start() {
        _uiState.update {
            it.copy(
                currentPuzzleIndex = 0,
                currentPuzzle = generateMathPuzzle(it.difficulty),
                allSolved = false
            )
        }
    }

    fun updateAnswer(newAnswer: String) {
        _uiState.update { it.copy(answer = newAnswer) }
    }

    fun submitAnswer(answer: String): Boolean {
        val current = _uiState.value.currentPuzzle
        val isCorrect = current.answer == answer.trim()

        if (isCorrect) {
            val nextIndex = _uiState.value.currentPuzzleIndex + 1
            val difficulty = _uiState.value.difficulty

            if (nextIndex < 3) {
                _uiState.update {
                    it.copy(
                        currentPuzzleIndex = nextIndex,
                        currentPuzzle = generateMathPuzzle(difficulty)
                    )
                }
                targetProgress.floatValue = 1f
            } else {
                _uiState.update { it.copy(allSolved = true) }
                allSolved() // <- side effect here
                return true // <- signal success
            }
        }
        return false
    }


    fun allSolved() {
        useCases.scheduleWakeUpCheckUseCase()
    }

    private fun generateMathPuzzle(level: Int): MathPuzzle {
        val range = when (level) {
            1 -> 1..10
            2 -> 5..20
            3 -> 10..50
            4 -> 20..100
            else -> 50..100
        }

        val a = range.random()
        val b = range.random()
        val question = "$a + $b"
        val answer = (a + b).toString()

        return MathPuzzle(question, answer)
    }
}