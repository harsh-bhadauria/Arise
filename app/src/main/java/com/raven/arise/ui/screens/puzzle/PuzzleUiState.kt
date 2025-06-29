package com.raven.arise.ui.screens.puzzle

data class MathPuzzle(val question: String, val answer: String)

data class PuzzleUiState(
    val difficulty: Int = 2,
    val puzzles: List<MathPuzzle> = emptyList(),
    val currentPuzzleIndex: Int = 0,
    val currentPuzzle: MathPuzzle = MathPuzzle("", ""),
    val answer: String = "",
    val allSolved: Boolean = false
)
