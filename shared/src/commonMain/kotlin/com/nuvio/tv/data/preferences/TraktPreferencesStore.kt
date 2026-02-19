package com.nuvio.tv.data.preferences

import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TraktPreferencesStore {
    private val _preferences = MutableStateFlow(TraktPreferences())
    val preferences: StateFlow<TraktPreferences> = _preferences.asStateFlow()

    private val _connectionMode = MutableStateFlow(TraktConnectionMode.DISCONNECTED)
    val connectionMode: StateFlow<TraktConnectionMode> = _connectionMode.asStateFlow()

    private val _deviceCode = MutableStateFlow<TraktDeviceCode?>(null)
    val deviceCode: StateFlow<TraktDeviceCode?> = _deviceCode.asStateFlow()

    fun updateAccessToken(token: String) {
        _preferences.value = _preferences.value.copy(accessToken = token)
        updateConnectionMode()
    }

    fun updateRefreshToken(token: String) {
        _preferences.value = _preferences.value.copy(refreshToken = token)
        updateConnectionMode()
    }

    fun updateTokenType(type: String) {
        _preferences.value = _preferences.value.copy(tokenType = type)
    }

    fun updateUsername(username: String) {
        _preferences.value = _preferences.value.copy(username = username)
    }

    fun updateUserSlug(slug: String) {
        _preferences.value = _preferences.value.copy(userSlug = slug)
    }

    fun updateCreatedAt(timestamp: Long) {
        _preferences.value = _preferences.value.copy(createdAt = timestamp)
    }

    fun updateExpiresIn(duration: Long) {
        _preferences.value = _preferences.value.copy(expiresIn = duration)
    }

    fun updateContinueWatchingDaysCap(days: Int) {
        _preferences.value = _preferences.value.copy(continueWatchingDaysCap = days)
    }

    fun setDeviceCode(code: TraktDeviceCode) {
        _deviceCode.value = code
        _connectionMode.value = TraktConnectionMode.AWAITING_APPROVAL
    }

    fun clearDeviceCode() {
        _deviceCode.value = null
        updateConnectionMode()
    }

    fun disconnect() {
        _preferences.value = TraktPreferences()
        _deviceCode.value = null
        _connectionMode.value = TraktConnectionMode.DISCONNECTED
    }

    private fun updateConnectionMode() {
        _connectionMode.value = when {
            _preferences.value.isAuthenticated -> TraktConnectionMode.CONNECTED
            _deviceCode.value != null -> TraktConnectionMode.AWAITING_APPROVAL
            else -> TraktConnectionMode.DISCONNECTED
        }
    }
}
