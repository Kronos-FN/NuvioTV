package com.nuvio.tv.ui.viewmodel

import com.nuvio.tv.data.preferences.PlayerPreferencesStore
import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.flow.StateFlow

class PlaybackSettingsViewModel(
    private val preferencesStore: PlayerPreferencesStore
) {
    val preferences: StateFlow<PlayerPreferences> = preferencesStore.preferences

    fun setPreferredPlayer(player: PlayerPreference) {
        preferencesStore.updatePreferredPlayer(player)
    }

    fun setAudioLanguage(language: String) {
        preferencesStore.updateAudioLanguage(language)
    }

    fun setSubtitleLanguage(language: String) {
        preferencesStore.updateSubtitleLanguage(language)
    }

    fun setSubtitleSize(size: Int) {
        preferencesStore.updateSubtitleSize(size)
    }

    fun setSubtitleStyle(style: SubtitleStyle) {
        preferencesStore.updateSubtitleStyle(style)
    }

    fun setDecoderPriority(priority: DecoderPriority) {
        preferencesStore.updateDecoderPriority(priority)
    }

    fun setAutoPlayNextEpisode(enabled: Boolean) {
        preferencesStore.updateAutoPlayNextEpisode(enabled)
    }

    fun setResumePlayback(enabled: Boolean) {
        preferencesStore.updateResumePlayback(enabled)
    }

    fun setSkipIntroEnabled(enabled: Boolean) {
        preferencesStore.updateSkipIntroEnabled(enabled)
    }

    fun setSkipOutroEnabled(enabled: Boolean) {
        preferencesStore.updateSkipOutroEnabled(enabled)
    }

    fun setTrailerAutoPlay(enabled: Boolean) {
        preferencesStore.updateTrailerAutoPlay(enabled)
    }
}
