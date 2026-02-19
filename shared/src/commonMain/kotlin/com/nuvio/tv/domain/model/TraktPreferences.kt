package com.nuvio.tv.domain.model

data class TraktPreferences(
    val accessToken: String = "",
    val refreshToken: String = "",
    val tokenType: String = "",
    val username: String = "",
    val userSlug: String = "",
    val createdAt: Long = 0L,
    val expiresIn: Long = 0L,
    val continueWatchingDaysCap: Int = 30
) {
    val isAuthenticated: Boolean
        get() = accessToken.isNotBlank() && refreshToken.isNotBlank()
}

enum class TraktConnectionMode {
    DISCONNECTED,
    AWAITING_APPROVAL,
    CONNECTED
}

data class TraktDeviceCode(
    val deviceCode: String = "",
    val userCode: String = "",
    val verificationUrl: String = "",
    val expiresIn: Int = 0,
    val interval: Int = 0
)
