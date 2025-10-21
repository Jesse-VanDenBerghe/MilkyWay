package com.jessevandenberghe.milkyway.ui.screens.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jessevandenberghe.milkyway.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimelineViewModel(private val sessionRepository: com.jessevandenberghe.milkyway.data.repository.ISessionRepository = RepositoryProvider.getSessionRepository()) : ViewModel() {
    private val _state = MutableStateFlow(TimelineState())
    val state = _state.asStateFlow()

    init {
        loadSessions()
    }

    private fun loadSessions() {
        viewModelScope.launch {
            sessionRepository.getSessions().collect { sessions ->
                _state.update { it.copy(sessions = sessions, isLoading = false) }
            }
        }
    }
}
