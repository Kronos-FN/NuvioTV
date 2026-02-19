package com.nuvio.tv.ui.navigation

/**
 * Navigation screens for the desktop app
 */
sealed class DesktopScreen {
    data object Home : DesktopScreen()
    data object Search : DesktopScreen()
    data object Addons : DesktopScreen()
    data object Settings : DesktopScreen()
    data object Library : DesktopScreen()
    
    data class Detail(
        val id: String,
        val type: String,
        val baseUrl: String? = null
    ) : DesktopScreen()
    
    data class Stream(
        val videoId: String,
        val contentType: String,
        val title: String,
        val season: Int? = null,
        val episode: Int? = null
    ) : DesktopScreen()
    
    data class Player(
        val streamUrl: String,
        val title: String,
        val headers: Map<String, String>? = null,
        val externalPlaybackNotice: String? = null
    ) : DesktopScreen()
}
