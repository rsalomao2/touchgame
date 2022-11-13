package com.salosoft.touchgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salosoft.touchgame.ui.widget.GridSizeOption
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var min = 0
    private var seconds = 0
    private var delay = 3000.0
    private var rounds = 0

    var hasStated = false
    var points = 0

    var timeFlow = MutableStateFlow("0:00")
    val positionFlow = MutableStateFlow(-1)
    val gridSizeFlow = MutableStateFlow(4)
    val lastSelectedItem = gridSizeFlow.map { size ->
        when (size) {
            4 -> GridSizeOption.FourByFour
            16 -> GridSizeOption.SixteenBySixteen
            36 -> GridSizeOption.ThirdSixByThirdSix
            64 -> GridSizeOption.SixtyFourBySixtyFour
            else -> GridSizeOption.SixtyFourBySixtyFour
        }
    }
    val errorMessageFlow = MutableStateFlow("")

    fun startStopHighlighting() {
        hasStated = !hasStated
        viewModelScope.launch {
            updateTicker()
            startHighlighting()
        }
    }

    fun onItemClicked(isHighlight: Boolean) {
        if (hasStated) {
            if (isHighlight) points++ else if (points > 0) points--
            if (points <= 0)
                gameOver()
        }
    }

    fun updateGridSize(size: Int) {
        viewModelScope.launch {
            gridSizeFlow.emit(size)
        }
    }

    private fun updateTicker() {
        if (hasStated) resetParameters()
        flow<Int> {
            while (hasStated) {
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
        return if (secondsString.length < 2) "0$secondsString" else secondsString
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
            while (hasStated) {
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
        val randomLimit = gridSizeFlow.first() - 1
        val position = Random.nextInt(0, randomLimit)
        val currentPosition = positionFlow.first()
        if (position == currentPosition) updateHighlight()
        positionFlow.emit(position)
    }

    private fun gameOver() {
        viewModelScope.launch {
            errorMessageFlow.emit("GAME OVER")
            startStopHighlighting()
        }
    }
}
