package com.salosoft.touchgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var min = 0
    private var seconds = 0

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
        flow<Int> {
            while (start) {
                delay(1000)
                updateSeconds()
                val formattedSeconds = getFormattedSeconds()
                timeFlow.emit("$min:$formattedSeconds")
            }
        }.launchIn(viewModelScope)
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
                delay(2000)
                val position = Random.nextInt(0, 15)
                positionFlow.emit(position)
            }
        }.launchIn(viewModelScope)
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
