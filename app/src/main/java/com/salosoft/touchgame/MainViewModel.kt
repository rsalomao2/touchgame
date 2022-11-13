package com.salosoft.touchgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var min = 0
    private var seconds = 0
    private var delay = 1000.0
    private var rounds = 0

    var start = false
    var points = 0

    var timeFlow = MutableStateFlow("0:00")
    val positionFlow = MutableStateFlow(-1)
    val errorMessageFlow = MutableStateFlow("")

    fun startStopHighlighting() {
        start = !start
        viewModelScope.launch {
            updateTicker()
            startHighlighting()
        }
    }

    private fun updateTicker() {
        if (start) resetParameters()
        flow<Int> {
            while (start) {
                delay(1000)
                updateSeconds()
                val formattedSeconds = getFormattedSeconds()
                timeFlow.emit("$min:$formattedSeconds")
            }
        }.launchIn(viewModelScope)
    }

    private fun resetParameters() {
        min = 0
        seconds = 0
        delay = 3000.0
        rounds = 0
        points = 0

        viewModelScope.launch {
            timeFlow.emit("0:00")
        }
    }

    private fun getFormattedSeconds(): String {
        val secondsString = seconds.toString()
        val formattedSeconds = if (secondsString.length < 2) "0$secondsString" else secondsString
        return formattedSeconds
    }

    private fun updateSeconds() {
        if (seconds < 60) seconds++
        else {
            updateMinutes()
            seconds = 0
        }
    }

    private fun updateMinutes() {
        if (min < 60) min++
        else min = 0
    }


    private fun startHighlighting() {
        flow<Int> {
            while (start) {
                updateRounds()
                updateHighlight()
                delay(delay.milliseconds)
                updateDelay()
            }
        }.launchIn(viewModelScope)
    }

    private fun updateRounds() {
        rounds++
    }

    private fun updateDelay() {
        if (rounds % 5 == 0 && delay > 200) {
            delay = delay.times(0.9)
        }
    }

    private suspend fun updateHighlight() {
        val position = Random.nextInt(0, 15)
        val currentPosition = positionFlow.first()
        if (position == currentPosition) updateHighlight()
        positionFlow.emit(position)
    }

    fun onItemClicked(isHighlight: Boolean) {
        if (isHighlight) points++ else if (points > 0) points-- else 0
        if (points <= 0)
            gameOver()
    }

    private fun gameOver() {
        viewModelScope.launch {
            errorMessageFlow.emit("GAME OVER")
            startStopHighlighting()
        }
    }
}
