package com.nuvio.tv.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse(
    @SerialName("meta") val meta: MetaDto? = null
)

@Serializable
data class MetaDto(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("poster") val poster: String? = null,
    @SerialName("posterShape") val posterShape: String? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("logo") val logo: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("releaseInfo") val releaseInfo: String? = null,
    @SerialName("imdbRating") val imdbRating: String? = null,
    @SerialName("genres") val genres: List<String>? = null,
    @SerialName("runtime") val runtime: String? = null,
    @SerialName("director") val director: List<String>? = null,
    @SerialName("writer") val writer: List<String>? = null,
    @SerialName("writers") val writers: List<String>? = null,
    @SerialName("cast") val cast: List<String>? = null,
    @SerialName("videos") val videos: List<VideoDto>? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("awards") val awards: String? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("links") val links: List<MetaLinkDto>? = null
)

@Serializable
data class VideoDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("released") val released: String? = null,
    @SerialName("thumbnail") val thumbnail: String? = null,
    @SerialName("season") val season: Int? = null,
    @SerialName("episode") val episode: Int? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("description") val description: String? = null
)

@Serializable
data class MetaLinkDto(
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("url") val url: String? = null
)
