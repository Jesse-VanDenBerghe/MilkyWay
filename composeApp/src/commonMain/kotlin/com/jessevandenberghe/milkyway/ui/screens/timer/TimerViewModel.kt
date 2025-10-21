package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jessevandenberghe.milkyway.data.model.FeedingSession
import com.jessevandenberghe.milkyway.data.repository.ISessionRepository
import com.jessevandenberghe.milkyway.data.repository.RepositoryProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TimerViewModel(
    private val coroutineScope: CoroutineScope? = null,
    private val sessionRepository: ISessionRepository = RepositoryProvider.getSessionRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private val scope: CoroutineScope
        get() = coroutineScope ?: viewModelScope

    private var timerJob: Job? = null

    private fun startTimer(
        step: TimingStep, updateElapsedTimeBy: (Duration) -> Unit
    ) {
        if (_state.value.timingStep != step) {
            _state.value = _state.value.copy(timingStep = step)
        } else {
            timerJob?.cancel()
        }

        timerJob = scope.launch {
            while (_state.value.timingStep == step) {
                delay(TIMING_STEP_DELAY)
                updateElapsedTimeBy(TIMING_STEP_DELAY)
            }
        }
    }

    private fun stopTimer(nextStep: TimingStep) {
        _state.value = _state.value.copy(timingStep = nextStep)
        timerJob?.cancel()
        timerJob = null
    }

    fun startFeeding() {
        // Capture start time when transitioning to feeding
        if (_state.value.sessionStartTime == 0L) {
            _state.value = _state.value.copy(sessionStartTime = Clock.System.now().toEpochMilliseconds())
        }
        
        startTimer(TimingStep.FEEDING) {
            val newElapsedTime = _state.value.elapsedFeedingTime + it
            val progress = (newElapsedTime.inWholeSeconds.toFloat() / _state.value.bottleTotalTime.inWholeSeconds.toFloat()).coerceIn(0f, 1f)
            val remainingProgress = 1f - progress
            val remainingMl = kotlin.math.max(0, (_state.value.bottleTotalMilliliters * remainingProgress).toInt())
            
            _state.value = _state.value.copy(
                elapsedFeedingTime = newElapsedTime,
                bottleRemainingMilliliters = remainingMl
            )
        }
    }

    fun stopFeeding() {
        stopTimer(TimingStep.BURPING)
    }

    fun startBurping() {
        startTimer(TimingStep.BURPING) {
            _state.value = _state.value.copy(
                elapsedBurpingTime = _state.value.elapsedBurpingTime + it
            )
        }
    }

    fun stopBurping() {
        stopTimer(TimingStep.FINISHED)
    }

    fun updateFinalBottleRemainingMilliliters(milliliters: Int) {
        _state.value = _state.value.copy(finalBottleRemainingMilliliters = milliliters)
    }
    
    fun updateFinalBottleRemainingMillilitersInput(input: String) {
        val ml = input.toIntOrNull() ?: 0
        _state.value = _state.value.copy(
            finalBottleRemainingMillilitersInput = input,
            finalBottleRemainingMilliliters = ml
        )
    }

    fun updateSessionQuality(quality: Float) {
        _state.value = _state.value.copy(sessionQuality = quality)
    }

    fun updateDrinkingSpeed(speed: Float) {
        _state.value = _state.value.copy(drinkingSpeed = speed)
    }

    fun finishSession() {
        stopTimer(TimingStep.FINISHED)
    }

    fun saveSession() {
        scope.launch {
            val milkConsumed = _state.value.bottleTotalMilliliters - _state.value.finalBottleRemainingMilliliters
            val session = FeedingSession(
                timestamp = _state.value.sessionStartTime,
                elapsedFeedingTime = _state.value.elapsedFeedingTime,
                elapsedBurpingTime = _state.value.elapsedBurpingTime,
                bottleTotalMilliliters = _state.value.bottleTotalMilliliters,
                milkConsumed = milkConsumed,
                finalBottleRemainingMilliliters = _state.value.finalBottleRemainingMilliliters,
                sessionQuality = _state.value.sessionQuality,
                drinkingSpeed = _state.value.drinkingSpeed,
                endTime = Clock.System.now().toEpochMilliseconds()
            )
            sessionRepository.saveSession(session)
        }
    }
    
    fun resetSession() {
        timerJob?.cancel()
        timerJob = null
        _state.value = TimerState()
    }

    fun updateBottleTotalMilliliters(milliliters: Int) {
        _state.value = _state.value.copy(
            bottleTotalMilliliters = milliliters,
            bottleRemainingMilliliters = milliliters
        )
    }

    fun updateBottleTotalMillilitersInput(input: String) {
        val ml = input.toIntOrNull()
        _state.value = _state.value.copy(
            bottleTotalMillilitersInput = input,
            bottleTotalMilliliters = ml ?: 0,
            bottleRemainingMilliliters = ml ?: 0
        )
    }

    fun updateBottleTotalTime(time: Duration) {
        _state.value = _state.value.copy(bottleTotalTime = time)
    }
    
    fun updateBottleTotalTimeInput(input: String) {
        val minutes = input.toLongOrNull()
        _state.value = _state.value.copy(
            bottleTotalTimeInput = input,
            bottleTotalTime = minutes?.let { it.minutes } ?: Duration.ZERO
        )
    }

    fun nextStep() {
        when (_state.value.timingStep) {
            TimingStep.SETUP -> startFeeding()
            TimingStep.FEEDING -> {
                stopFeeding()
                startBurping()
            }
            TimingStep.BURPING -> stopBurping()
            TimingStep.SUMMARY -> { /* Summary can be skipped */ }
            TimingStep.FINISHED -> { /* Already at end */ }
        }
    }

    fun previousStep() {
        when (_state.value.timingStep) {
            TimingStep.SETUP -> { /* Already at beginning */ }
            TimingStep.FEEDING -> {
                timerJob?.cancel()
                timerJob = null
                _state.value = _state.value.copy(
                    timingStep = TimingStep.SETUP,
                    elapsedFeedingTime = Duration.ZERO,
                    bottleRemainingMilliliters = _state.value.bottleTotalMilliliters
                )
            }
            TimingStep.BURPING -> {
                timerJob?.cancel()
                timerJob = null
                _state.value = _state.value.copy(
                    timingStep = TimingStep.FEEDING,
                    elapsedBurpingTime = Duration.ZERO
                )
                startFeeding()
            }
            TimingStep.SUMMARY -> {
                _state.value = _state.value.copy(
                    timingStep = TimingStep.BURPING
                )
                startBurping()
            }
            TimingStep.FINISHED -> {
                _state.value = _state.value.copy(
                    timingStep = TimingStep.BURPING
                )
                startBurping()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        val TIMING_STEP_DELAY = 1.seconds
    }
}