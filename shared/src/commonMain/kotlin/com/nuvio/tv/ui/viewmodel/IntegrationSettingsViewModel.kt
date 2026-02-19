package com.nuvio.tv.ui.viewmodel

import com.nuvio.tv.data.preferences.IntegrationPreferencesStore
import com.nuvio.tv.domain.model.IntegrationPreferences
import kotlinx.coroutines.flow.StateFlow

class IntegrationSettingsViewModel(
    private val preferencesStore: IntegrationPreferencesStore
) {
    val preferences: StateFlow<IntegrationPreferences> = preferencesStore.preferences

    // TMDB
    fun setTmdbEnabled(enabled: Boolean) {
        preferencesStore.updateTmdbEnabled(enabled)
    }

    fun setTmdbLanguage(language: String) {
        preferencesStore.updateTmdbLanguage(language)
    }

    fun setTmdbDescriptions(enabled: Boolean) {
        preferencesStore.updateTmdbDescriptions(enabled)
    }

    fun setTmdbRatings(enabled: Boolean) {
        preferencesStore.updateTmdbRatings(enabled)
    }

    fun setTmdbPosters(enabled: Boolean) {
        preferencesStore.updateTmdbPosters(enabled)
    }

    fun setTmdbBackdrops(enabled: Boolean) {
        preferencesStore.updateTmdbBackdrops(enabled)
    }

    fun setTmdbLogos(enabled: Boolean) {
        preferencesStore.updateTmdbLogos(enabled)
    }

    // MDBList
    fun setMdbListApiKey(key: String) {
        preferencesStore.updateMdbListApiKey(key)
    }

    fun setMdbListEnabled(enabled: Boolean) {
        preferencesStore.updateMdbListEnabled(enabled)
    }

    // Real-Debrid
    fun setRealDebridApiKey(key: String) {
        preferencesStore.updateRealDebridApiKey(key)
    }

    fun setRealDebridEnabled(enabled: Boolean) {
        preferencesStore.updateRealDebridEnabled(enabled)
    }
}
