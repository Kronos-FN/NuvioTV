package com.nuvio.tv.data.preferences

import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerPreferencesStore {
    private val _preferences = MutableStateFlow(PlayerPreferences())
    val preferences: StateFlow<PlayerPreferences> = _preferences.asStateFlow()

    fun updatePreferredPlayer(player: PlayerPreference) {
        _preferences.value = _preferences.value.copy(preferredPlayer = player)
    }

    fun updateAudioLanguage(language: String) {
        _preferences.value = _preferences.value.copy(audioLanguage = language)
    }

    fun updateSubtitleLanguage(language: String) {
        _preferences.value = _preferences.value.copy(subtitleLanguage = language)
    }

    fun updateSubtitleSize(size: Int) {
        _preferences.value = _preferences.value.copy(subtitleSize = size)
    }

    fun updateSubtitleStyle(style: SubtitleStyle) {
        _preferences.value = _preferences.value.copy(subtitleStyle = style)
    }

    fun updateDecoderPriority(priority: DecoderPriority) {
        _preferences.value = _preferences.value.copy(decoderPriority = priority)
    }

    fun updateAutoPlayNextEpisode(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(autoPlayNextEpisode = enabled)
    }

    fun updateResumePlayback(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(resumePlayback = enabled)
    }

    fun updateSkipIntroEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(skipIntroEnabled = enabled)
    }

    fun updateSkipOutroEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(skipOutroEnabled = enabled)
    }

    fun updateTrailerAutoPlay(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(trailerAutoPlay = enabled)
    }
}
