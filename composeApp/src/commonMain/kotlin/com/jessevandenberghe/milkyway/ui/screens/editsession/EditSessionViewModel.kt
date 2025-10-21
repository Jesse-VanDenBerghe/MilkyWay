package com.jessevandenberghe.milkyway.ui.screens.editsession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jessevandenberghe.milkyway.data.model.FeedingSession
import com.jessevandenberghe.milkyway.data.repository.ISessionRepository
import com.jessevandenberghe.milkyway.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class EditSessionViewModel(
    private val sessionId: String,
    private val repository: ISessionRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(EditSessionState())
    val state = _state.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            val sessions = repository.getSessions().first()
            val session = sessions.firstOrNull { it.id == sessionId }
            
            if (session != null) {
                _state.value = EditSessionState(
                    session = session,
                    isLoading = false,
                    feedingTimeInput = "${session.elapsedFeedingTime.inWholeMinutes}",
                    burpingTimeInput = "${session.elapsedBurpingTime.inWholeMinutes}",
                    bottleTotalMillilitersInput = "${session.bottleTotalMilliliters}",
                    finalBottleRemainingMillilitersInput = "${session.finalBottleRemainingMilliliters}",
                    sessionQuality = session.sessionQuality,
                    drinkingSpeed = session.drinkingSpeed
                )
            } else {
                _state.value = EditSessionState(
                    isLoading = false,
                    session = null
                )
            }
        }
    }

    fun updateFeedingTime(input: String) {
        _state.value = _state.value.copy(
            feedingTimeInput = input,
            hasChanges = true
        )
    }

    fun updateBurpingTime(input: String) {
        _state.value = _state.value.copy(
            burpingTimeInput = input,
            hasChanges = true
        )
    }

    fun updateBottleTotalMilliliters(input: String) {
        _state.value = _state.value.copy(
            bottleTotalMillilitersInput = input,
            hasChanges = true
        )
    }

    fun updateFinalBottleRemainingMilliliters(input: String) {
        _state.value = _state.value.copy(
            finalBottleRemainingMillilitersInput = input,
            hasChanges = true
        )
    }

    fun updateSessionQuality(quality: Float) {
        _state.value = _state.value.copy(
            sessionQuality = quality,
            hasChanges = true
        )
    }

    fun updateDrinkingSpeed(speed: Float) {
        _state.value = _state.value.copy(
            drinkingSpeed = speed,
            hasChanges = true
        )
    }

    fun saveChanges(onSuccess: () -> Unit) {
        val currentSession = _state.value.session ?: return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            
            val feedingMinutes = _state.value.feedingTimeInput.toIntOrNull() ?: 0
            val burpingMinutes = _state.value.burpingTimeInput.toIntOrNull() ?: 0
            val bottleTotal = _state.value.bottleTotalMillilitersInput.toIntOrNull() ?: 0
            val bottleRemaining = _state.value.finalBottleRemainingMillilitersInput.toIntOrNull() ?: 0
            
            val updatedSession = currentSession.copy(
                elapsedFeedingTime = feedingMinutes.minutes,
                elapsedBurpingTime = burpingMinutes.minutes,
                bottleTotalMilliliters = bottleTotal,
                finalBottleRemainingMilliliters = bottleRemaining,
                milkConsumed = bottleTotal - bottleRemaining,
                sessionQuality = _state.value.sessionQuality,
                drinkingSpeed = _state.value.drinkingSpeed
            )
            
            repository.saveSession(updatedSession)
            
            _state.value = _state.value.copy(
                isSaving = false,
                hasChanges = false
            )
            
            onSuccess()
        }
    }
}
