package com.nuvio.tv.ui.viewmodel

import com.nuvio.tv.data.preferences.TraktPreferencesStore
import com.nuvio.tv.data.remote.api.TraktApi
import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TraktViewModel(
    private val preferencesStore: TraktPreferencesStore,
    private val traktApi: TraktApi,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    val preferences: StateFlow<TraktPreferences> = preferencesStore.preferences
    val connectionMode: StateFlow<TraktConnectionMode> = preferencesStore.connectionMode
    val deviceCode: StateFlow<TraktDeviceCode?> = preferencesStore.deviceCode
    
    private val _isPolling = MutableStateFlow(false)
    val isPolling: StateFlow<Boolean> = _isPolling.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _userProfile = MutableStateFlow<String?>(null) // Username display
    val userProfile: StateFlow<String?> = _userProfile.asStateFlow()

    init {
        // Load user profile if already authenticated
        if (preferences.value.isAuthenticated) {
            loadUserProfile()
        }
    }

    fun startDeviceCodeFlow() {
        coroutineScope.launch {
            _error.value = null
            val result = traktApi.getDeviceCode()
            result.onSuccess { response ->
                val code = TraktDeviceCode(
                    deviceCode = response.deviceCode,
                    userCode = response.userCode,
                    verificationUrl = response.verificationUrl,
                    expiresIn = response.expiresIn,
                    interval = response.interval
                )
                preferencesStore.setDeviceCode(code)
                startPollingForToken(code)
            }.onFailure { e ->
                _error.value = "Failed to get device code: ${e.message}"
            }
        }
    }
    
    private fun startPollingForToken(code: TraktDeviceCode) {
        coroutineScope.launch {
            _isPolling.value = true
            val startTime = System.currentTimeMillis()
            val expiryTime = startTime + (code.expiresIn * 1000)
            
            while (_isPolling.value && System.currentTimeMillis() < expiryTime) {
                delay((code.interval * 1000).toLong())
                
                val result = traktApi.pollForToken(code.deviceCode)
                result.onSuccess { response ->
                    // Token received! Save and get user profile
                    preferencesStore.updateAccessToken(response.accessToken)
                    preferencesStore.updateRefreshToken(response.refreshToken)
                    preferencesStore.updateTokenType(response.tokenType)
                    preferencesStore.updateCreatedAt(response.createdAt)
                    preferencesStore.updateExpiresIn(response.expiresIn)
                    preferencesStore.clearDeviceCode()
                    
                    // Get user profile
                    loadUserProfile()
                    
                    _isPolling.value = false
                    _error.value = null
                }.onFailure { e ->
                    // Keep polling unless it's a different error than "pending"
                    if (!e.message.orEmpty().contains("pending", ignoreCase = true)) {
                        _error.value = "Polling error: ${e.message}"
                    }
                }
            }
            
            if (System.currentTimeMillis() >= expiryTime) {
                _error.value = "Device code expired. Please try again."
                preferencesStore.clearDeviceCode()
            }
            
            _isPolling.value = false
        }
    }
    
    fun stopPolling() {
        _isPolling.value = false
    }
    
    private fun loadUserProfile() {
        coroutineScope.launch {
            val accessToken = preferences.value.accessToken
            if (accessToken.isNotBlank()) {
                val result = traktApi.getUserProfile(accessToken)
                result.onSuccess { profile ->
                    preferencesStore.updateUsername(profile.username)
                    preferencesStore.updateUserSlug(profile.ids.slug)
                    _userProfile.value = profile.name ?: profile.username
                }.onFailure { e ->
                    _error.value = "Failed to load profile: ${e.message}"
                }
            }
        }
    }

    fun setContinueWatchingDaysCap(days: Int) {
        preferencesStore.updateContinueWatchingDaysCap(days)
    }

    fun disconnect() {
        stopPolling()
        preferencesStore.disconnect()
        _userProfile.value = null
        _error.value = null
    }

    fun clearError() {
        _error.value = null
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
        loadUserProfile()
    }
}
