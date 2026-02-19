package com.nuvio.tv.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Trakt API OAuth device code response.
 */
@Serializable
data class TraktDeviceCodeResponse(
    @SerialName("device_code") val deviceCode: String,
    @SerialName("user_code") val userCode: String,
    @SerialName("verification_url") val verificationUrl: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("interval") val interval: Int
)

/**
 * Trakt API OAuth token response.
 */
@Serializable
data class TraktTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("scope") val scope: String,
    @SerialName("created_at") val createdAt: Long
)

/**
 * Trakt user profile.
 */
@Serializable
data class TraktUserProfile(
    @SerialName("username") val username: String,
    @SerialName("private") val private: Boolean = false,
    @SerialName("name") val name: String? = null,
    @SerialName("vip") val vip: Boolean = false,
    @SerialName("vip_ep") val vipEp: Boolean = false,
    @SerialName("ids") val ids: TraktUserIds
)

@Serializable
data class TraktUserIds(
    @SerialName("slug") val slug: String
)

/**
 * Trakt watchlist item.
 */
@Serializable
data class TraktWatchlistItem(
    @SerialName("rank") val rank: Int? = null,
    @SerialName("listed_at") val listedAt: String,
    @SerialName("type") val type: String,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null
)

/**
 * Trakt collection item.
 */
@Serializable
data class TraktCollectionItem(
    @SerialName("collected_at") val collectedAt: String,
    @SerialName("type") val type: String,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null
)

/**
 * Trakt custom list.
 */
@Serializable
data class TraktList(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("privacy") val privacy: String,
    @SerialName("display_numbers") val displayNumbers: Boolean = false,
    @SerialName("allow_comments") val allowComments: Boolean = false,
    @SerialName("sort_by") val sortBy: String = "rank",
    @SerialName("sort_how") val sortHow: String = "asc",
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("item_count") val itemCount: Int,
    @SerialName("comment_count") val commentCount: Int,
    @SerialName("likes") val likes: Int,
    @SerialName("ids") val ids: TraktListIds,
    @SerialName("user") val user: TraktListUser? = null
)

@Serializable
data class TraktListIds(
    @SerialName("trakt") val trakt: Long,
    @SerialName("slug") val slug: String
)

@Serializable
data class TraktListUser(
    @SerialName("username") val username: String,
    @SerialName("private") val private: Boolean,
    @SerialName("name") val name: String? = null,
    @SerialName("vip") val vip: Boolean = false,
    @SerialName("vip_ep") val vipEp: Boolean = false
)

/**
 * Trakt list item.
 */
@Serializable
data class TraktListItem(
    @SerialName("rank") val rank: Int,
    @SerialName("listed_at") val listedAt: String,
    @SerialName("type") val type: String,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null
)

/**
 * Trakt movie metadata.
 */
@Serializable
data class TraktMovie(
    @SerialName("title") val title: String,
    @SerialName("year") val year: Int? = null,
    @SerialName("ids") val ids: TraktMovieIds
)

@Serializable
data class TraktMovieIds(
    @SerialName("trakt") val trakt: Long,
    @SerialName("slug") val slug: String,
    @SerialName("imdb") val imdb: String? = null,
    @SerialName("tmdb") val tmdb: Long? = null
)

/**
 * Trakt show metadata.
 */
@Serializable
data class TraktShow(
    @SerialName("title") val title: String,
    @SerialName("year") val year: Int? = null,
    @SerialName("ids") val ids: TraktShowIds
)

@Serializable
data class TraktShowIds(
    @SerialName("trakt") val trakt: Long,
    @SerialName("slug") val slug: String,
    @SerialName("tvdb") val tvdb: Long? = null,
    @SerialName("imdb") val imdb: String? = null,
    @SerialName("tmdb") val tmdb: Long? = null
)

/**
 * Trakt scrobble request.
 */
@Serializable
data class TraktScrobbleRequest(
    @SerialName("movie") val movie: TraktMovieIds? = null,
    @SerialName("show") val show: TraktShowIds? = null,
    @SerialName("episode") val episode: TraktEpisodeIds? = null,
    @SerialName("progress") val progress: Float,
    @SerialName("app_version") val appVersion: String = "1.0",
    @SerialName("app_date") val appDate: String = "2026-01-01"
)

@Serializable
data class TraktEpisodeIds(
    @SerialName("trakt") val trakt: Long? = null,
    @SerialName("tvdb") val tvdb: Long? = null,
    @SerialName("imdb") val imdb: String? = null,
    @SerialName("tmdb") val tmdb: Long? = null
)

/**
 * Trakt scrobble response.
 */
@Serializable
data class TraktScrobbleResponse(
    @SerialName("id") val id: Long,
    @SerialName("action") val action: String,
    @SerialName("progress") val progress: Float,
    @SerialName("sharing") val sharing: TraktSharing? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null
)

@Serializable
data class TraktSharing(
    @SerialName("twitter") val twitter: Boolean = false,
    @SerialName("tumblr") val tumblr: Boolean = false
)

@Serializable
data class TraktEpisode(
    @SerialName("season") val season: Int,
    @SerialName("number") val number: Int,
    @SerialName("title") val title: String,
    @SerialName("ids") val ids: TraktEpisodeIds
)

/**
 * Trakt watched progress response.
 */
@Serializable
data class TraktProgressResponse(
    @SerialName("aired") val aired: Int,
    @SerialName("completed") val completed: Int,
    @SerialName("last_watched_at") val lastWatchedAt: String? = null,
    @SerialName("reset_at") val resetAt: String? = null,
    @SerialName("seasons") val seasons: List<TraktSeasonProgress> = emptyList(),
    @SerialName("hidden_seasons") val hiddenSeasons: List<TraktSeasonProgress> = emptyList(),
    @SerialName("next_episode") val nextEpisode: TraktEpisode? = null,
    @SerialName("last_episode") val lastEpisode: TraktEpisode? = null
)

@Serializable
data class TraktSeasonProgress(
    @SerialName("number") val number: Int,
    @SerialName("aired") val aired: Int,
    @SerialName("completed") val completed: Int,
    @SerialName("episodes") val episodes: List<TraktEpisodeProgress> = emptyList()
)

@Serializable
data class TraktEpisodeProgress(
    @SerialName("number") val number: Int,
    @SerialName("completed") val completed: Boolean,
    @SerialName("last_watched_at") val lastWatchedAt: String? = null
)
