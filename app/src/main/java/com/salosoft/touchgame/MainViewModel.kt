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
    var time = "0:00"

    val positionFlow = MutableStateFlow(-1)
    val errorMessage = MutableStateFlow("")

    fun startStopHighlighting() {
        start = !start
        viewModelScope.launch {
            startHighlighting()
        }
    }

    private fun updateTicker() {
        updateSeconds()
        val formattedSeconds = getFormattedSeconds()
        time = "$min:$formattedSeconds"
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
                delay(1000)
                updateTicker()
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
            errorMessage.emit("GAME OVER")
            startStopHighlighting()

        }
    }
}
