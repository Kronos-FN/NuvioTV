package com.nuvio.tv.data.preferences

import com.nuvio.tv.domain.model.IntegrationPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IntegrationPreferencesStore {
    private val _preferences = MutableStateFlow(IntegrationPreferences())
    val preferences: StateFlow<IntegrationPreferences> = _preferences.asStateFlow()

    fun updateTmdbEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(tmdbEnabled = enabled)
    }

    fun updateTmdbLanguage(language: String) {
        _preferences.value = _preferences.value.copy(tmdbLanguage = language)
    }

    fun updateTmdbDescriptions(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(tmdbDescriptions = enabled)
    }

    fun updateTmdbRatings(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(tmdbRatings = enabled)
    }

    fun updateTmdbPosters(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(tmdbPosters = enabled)
    }

    fun updateTmdbBackdrops(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(tmdbBackdrops = enabled)
    }

    fun updateTmdbLogos(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(tmdbLogos = enabled)
    }

    fun updateMdbListApiKey(key: String) {
        _preferences.value = _preferences.value.copy(mdbListApiKey = key)
    }

    fun updateMdbListEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(mdbListEnabled = enabled)
    }

    fun updateRealDebridApiKey(key: String) {
        _preferences.value = _preferences.value.copy(realDebridApiKey = key)
    }

    fun updateRealDebridEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(realDebridEnabled = enabled)
    }
}
