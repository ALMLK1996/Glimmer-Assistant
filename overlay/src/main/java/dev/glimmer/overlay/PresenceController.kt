package dev.glimmer.overlay

import dev.glimmer.core.model.LightBeingState
import dev.glimmer.core.model.PresenceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PresenceController(private val scope: CoroutineScope) {
    private val _state = MutableStateFlow(LightBeingState())
    val state: StateFlow<LightBeingState> = _state.asStateFlow()
    private var fadeJob: Job? = null
    private val fadeDurationMs = 350L

    fun onUserInteraction() {
        fadeJob?.cancel()
        _state.value = _state.value.copy(
            presence = PresenceState.VISIBLE,
            isListening = false,
            isThinking = false
        )
    }

    fun onUserBrowsing() {
        fadeJob?.cancel()
        fadeJob = scope.launch {
            _state.value = _state.value.copy(presence = PresenceState.FADING, isListening = false)
            delay(fadeDurationMs)
            _state.value = _state.value.copy(presence = PresenceState.HIDDEN)
        }
    }

    fun onThinking() {
        _state.value = _state.value.copy(isThinking = true, density = 0.85f)
    }

    fun onFinishedThinking() {
        _state.value = _state.value.copy(isThinking = false, density = 0.6f)
    }

    fun forceHide() {
        fadeJob?.cancel()
        _state.value = LightBeingState(presence = PresenceState.HIDDEN)
    }
}
