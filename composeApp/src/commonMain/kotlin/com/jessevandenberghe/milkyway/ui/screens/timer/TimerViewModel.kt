package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimerViewModel(
    private val coroutineScope: CoroutineScope? = null
) : ViewModel() {

    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private val scope: CoroutineScope
        get() = coroutineScope ?: viewModelScope

    private var timerJob: Job? = null

    private fun startTimer(
        step: TimingStep, updateElapsedTimeBy: (Duration) -> Unit
    ) {
        if (_state.value.timingStep == step) return

        _state.value = _state.value.copy(timingStep = step)
        timerJob = scope.launch {
            while (_state.value.timingStep == step) {
                delay(TIMING_STEP_DELAY)
                updateElapsedTimeBy(TIMING_STEP_DELAY)
            }
        }
    }

    private fun stopTimer() {
        _state.value = _state.value.copy(timingStep = TimingStep.IDLE)
        timerJob?.cancel()
        timerJob = null
    }

    fun startFeeding() {
        startTimer(TimingStep.FEEDING) {
            _state.value = _state.value.copy(
                elapsedFeedingTime = _state.value.elapsedFeedingTime + it
            )
        }
    }

    fun stopFeeding() {
        stopTimer()
    }

    fun startBurping() {
        startTimer(TimingStep.BURPING) {
            _state.value = _state.value.copy(
                elapsedBurpingTime = _state.value.elapsedBurpingTime + it
            )
        }
    }

    fun stopBurping() {
        stopTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        val TIMING_STEP_DELAY = 1.seconds
    }
}