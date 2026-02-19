package com.nuvio.tv.data.preferences

import com.nuvio.tv.data.persistence.JsonFileStorage
import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.serializer

class TraktPreferencesStore(
    private val storage: JsonFileStorage = JsonFileStorage()
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private val _preferences = MutableStateFlow(TraktPreferences())
    val preferences: StateFlow<TraktPreferences> = _preferences.asStateFlow()

    private val _connectionMode = MutableStateFlow(TraktConnectionMode.DISCONNECTED)
    val connectionMode: StateFlow<TraktConnectionMode> = _connectionMode.asStateFlow()

    private val _deviceCode = MutableStateFlow<TraktDeviceCode?>(null)
    val deviceCode: StateFlow<TraktDeviceCode?> = _deviceCode.asStateFlow()

    init {
        // Load preferences on initialization
        scope.launch {
            loadFromStorage()
        }
    }

    private suspend fun loadFromStorage() {
        val stored = storage.read(PREFS_KEY, serializer<TraktPreferences>())
        if (stored != null) {
            _preferences.value = stored
            updateConnectionMode()
        }
    }

    private suspend fun saveToStorage() {
        storage.write(PREFS_KEY, _preferences.value, serializer())
    }

    fun updateAccessToken(token: String) {
        scope.launch {
            _preferences.value = _preferences.value.copy(accessToken = token)
            saveToStorage()
            updateConnectionMode()
        }
    }

    fun updateRefreshToken(token: String) {
        scope.launch {
            _preferences.value = _preferences.value.copy(refreshToken = token)
            saveToStorage()
            updateConnectionMode()
        }
    }

    fun updateTokenType(type: String) {
        scope.launch {
            _preferences.value = _preferences.value.copy(tokenType = type)
            saveToStorage()
        }
    }

    fun updateUsername(username: String) {
        scope.launch {
            _preferences.value = _preferences.value.copy(username = username)
            saveToStorage()
        }
    }

    fun updateUserSlug(slug: String) {
        scope.launch {
            _preferences.value = _preferences.value.copy(userSlug = slug)
            saveToStorage()
        }
    }

    fun updateCreatedAt(timestamp: Long) {
        scope.launch {
            _preferences.value = _preferences.value.copy(createdAt = timestamp)
            saveToStorage()
        }
    }

    fun updateExpiresIn(duration: Long) {
        scope.launch {
            _preferences.value = _preferences.value.copy(expiresIn = duration)
            saveToStorage()
        }
    }

    fun updateContinueWatchingDaysCap(days: Int) {
        scope.launch {
            _preferences.value = _preferences.value.copy(continueWatchingDaysCap = days)
            saveToStorage()
        }
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
        scope.launch {
            _preferences.value = TraktPreferences()
            _deviceCode.value = null
            _connectionMode.value = TraktConnectionMode.DISCONNECTED
            saveToStorage()
        }
    }

    private fun updateConnectionMode() {
        _connectionMode.value = when {
            _preferences.value.isAuthenticated -> TraktConnectionMode.CONNECTED
            _deviceCode.value != null -> TraktConnectionMode.AWAITING_APPROVAL
            else -> TraktConnectionMode.DISCONNECTED
        }
    }

    companion object {
        private const val PREFS_KEY = "trakt_preferences"
    }
}
