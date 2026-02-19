package com.nuvio.tv.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Stream(
    val name: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val ytId: String?,
    val infoHash: String?,
    val fileIdx: Int?,
    val externalUrl: String?,
    val behaviorHints: StreamBehaviorHints?,
    val addonName: String,
    val addonLogo: String?
) {
    fun getStreamUrl(): String? = url ?: externalUrl
    fun isTorrent(): Boolean = infoHash != null
    fun isYouTube(): Boolean = ytId != null
    fun isExternal(): Boolean = externalUrl != null && url == null
    fun getDisplayName(): String = name ?: title ?: description ?: "Unknown Stream"
    fun getDisplayDescription(): String? = description ?: title
}

@Immutable
data class StreamBehaviorHints(
    val notWebReady: Boolean?,
    val bingeGroup: String?,
    val countryWhitelist: List<String>?,
    val proxyHeaders: ProxyHeaders?
)

@Immutable
data class ProxyHeaders(
    val request: Map<String, String>?,
    val response: Map<String, String>?
)

@Immutable
data class AddonStreams(
    val addonName: String,
    val addonLogo: String?,
    val streams: List<Stream>
)
