package com.nuvio.tv.ui.viewmodel

import com.nuvio.tv.data.preferences.TraktPreferencesStore
import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.flow.StateFlow

class TraktViewModel(
    private val preferencesStore: TraktPreferencesStore
) {
    val preferences: StateFlow<TraktPreferences> = preferencesStore.preferences
    val connectionMode: StateFlow<TraktConnectionMode> = preferencesStore.connectionMode
    val deviceCode: StateFlow<TraktDeviceCode?> = preferencesStore.deviceCode

    fun startDeviceCodeFlow() {
        // In a real implementation, this would call the Trakt API
        // For now, we'll set a placeholder device code
        val code = TraktDeviceCode(
            deviceCode = "DEVICE123",
            userCode = "ABCD-EFGH",
            verificationUrl = "https://trakt.tv/activate",
            expiresIn = 600,
            interval = 5
        )
        preferencesStore.setDeviceCode(code)
    }

    fun setContinueWatchingDaysCap(days: Int) {
        preferencesStore.updateContinueWatchingDaysCap(days)
    }

    fun disconnect() {
        preferencesStore.disconnect()
    }

    fun authenticateWithToken(
        accessToken: String,
        refreshToken: String,
        username: String
    ) {
        preferencesStore.updateAccessToken(accessToken)
        preferencesStore.updateRefreshToken(refreshToken)
        preferencesStore.updateUsername(username)
        preferencesStore.updateTokenType("Bearer")
        preferencesStore.updateCreatedAt(System.currentTimeMillis())
        preferencesStore.updateExpiresIn(7776000) // 90 days in seconds
        preferencesStore.clearDeviceCode()
    }
}
