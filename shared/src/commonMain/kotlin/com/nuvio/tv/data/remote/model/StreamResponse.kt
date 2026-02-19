package com.nuvio.tv.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamResponse(
    @SerialName("streams") val streams: List<StreamResponseDto>? = null
)

@Serializable
data class StreamResponseDto(
    @SerialName("name") val name: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("ytId") val ytId: String? = null,
    @SerialName("infoHash") val infoHash: String? = null,
    @SerialName("fileIdx") val fileIdx: Int? = null,
    @SerialName("externalUrl") val externalUrl: String? = null,
    @SerialName("behaviorHints") val behaviorHints: BehaviorHintsResponse? = null
)

@Serializable
data class BehaviorHintsResponse(
    @SerialName("notWebReady") val notWebReady: Boolean? = null,
    @SerialName("bingeGroup") val bingeGroup: String? = null,
    @SerialName("countryWhitelist") val countryWhitelist: List<String>? = null,
    @SerialName("proxyHeaders") val proxyHeaders: ProxyHeadersResponse? = null
)

@Serializable
data class ProxyHeadersResponse(
    @SerialName("request") val request: Map<String, String>? = null,
    @SerialName("response") val response: Map<String, String>? = null
)
