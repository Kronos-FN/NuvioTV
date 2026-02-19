package com.nuvio.tv.data.remote.api

import com.nuvio.tv.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Trakt.tv API client.
 * Implements OAuth device code flow and various API endpoints.
 */
class TraktApi(
    private val httpClient: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://api.trakt.tv"
        private const val API_VERSION = "2"
        
        // TODO: These should be moved to a secure configuration
        // For now, using placeholder values - actual values should come from Trakt app registration
        private const val CLIENT_ID = "YOUR_CLIENT_ID_HERE"
        private const val CLIENT_SECRET = "YOUR_CLIENT_SECRET_HERE"
    }
    
    /**
     * Get device code for OAuth flow.
     * User must visit verification_url and enter user_code.
     */
    suspend fun getDeviceCode(): Result<TraktDeviceCodeResponse> = try {
        val response = httpClient.post("$BASE_URL/oauth/device/code") {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(mapOf("client_id" to CLIENT_ID))
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktDeviceCodeResponse>())
        } else {
            Result.failure(Exception("Failed to get device code: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Poll for OAuth token.
     * Should be called repeatedly with device_code until user approves.
     */
    suspend fun pollForToken(deviceCode: String): Result<TraktTokenResponse> = try {
        val response = httpClient.post("$BASE_URL/oauth/device/token") {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(mapOf(
                "code" to deviceCode,
                "client_id" to CLIENT_ID,
                "client_secret" to CLIENT_SECRET
            ))
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktTokenResponse>())
        } else {
            Result.failure(Exception("Token not ready yet or expired: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Refresh OAuth token.
     */
    suspend fun refreshToken(refreshToken: String): Result<TraktTokenResponse> = try {
        val response = httpClient.post("$BASE_URL/oauth/token") {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(mapOf(
                "refresh_token" to refreshToken,
                "client_id" to CLIENT_ID,
                "client_secret" to CLIENT_SECRET,
                "grant_type" to "refresh_token"
            ))
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktTokenResponse>())
        } else {
            Result.failure(Exception("Failed to refresh token: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Get user profile.
     */
    suspend fun getUserProfile(accessToken: String): Result<TraktUserProfile> = try {
        val response = httpClient.get("$BASE_URL/users/me") {
            headers {
                appendTraktHeaders(accessToken)
            }
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktUserProfile>())
        } else {
            Result.failure(Exception("Failed to get user profile: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Get watchlist (movies and shows).
     */
    suspend fun getWatchlist(accessToken: String, type: String? = null): Result<List<TraktWatchlistItem>> = try {
        val typeParam = type?.let { "/$it" } ?: ""
        val response = httpClient.get("$BASE_URL/sync/watchlist$typeParam") {
            headers {
                appendTraktHeaders(accessToken)
            }
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<List<TraktWatchlistItem>>())
        } else {
            Result.failure(Exception("Failed to get watchlist: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Get collection (movies and shows).
     */
    suspend fun getCollection(accessToken: String, type: String? = null): Result<List<TraktCollectionItem>> = try {
        val typeParam = type?.let { "/$it" } ?: ""
        val response = httpClient.get("$BASE_URL/sync/collection$typeParam") {
            headers {
                appendTraktHeaders(accessToken)
            }
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<List<TraktCollectionItem>>())
        } else {
            Result.failure(Exception("Failed to get collection: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Get user's custom lists.
     */
    suspend fun getCustomLists(accessToken: String, username: String): Result<List<TraktList>> = try {
        val response = httpClient.get("$BASE_URL/users/$username/lists") {
            headers {
                appendTraktHeaders(accessToken)
            }
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<List<TraktList>>())
        } else {
            Result.failure(Exception("Failed to get custom lists: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Get items from a custom list.
     */
    suspend fun getListItems(accessToken: String, username: String, listId: String): Result<List<TraktListItem>> = try {
        val response = httpClient.get("$BASE_URL/users/$username/lists/$listId/items") {
            headers {
                appendTraktHeaders(accessToken)
            }
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<List<TraktListItem>>())
        } else {
            Result.failure(Exception("Failed to get list items: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Start scrobble (watching).
     */
    suspend fun startScrobble(accessToken: String, request: TraktScrobbleRequest): Result<TraktScrobbleResponse> = try {
        val response = httpClient.post("$BASE_URL/scrobble/start") {
            headers {
                appendTraktHeaders(accessToken)
                append("Content-Type", "application/json")
            }
            setBody(request)
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktScrobbleResponse>())
        } else {
            Result.failure(Exception("Failed to start scrobble: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Pause scrobble.
     */
    suspend fun pauseScrobble(accessToken: String, request: TraktScrobbleRequest): Result<TraktScrobbleResponse> = try {
        val response = httpClient.post("$BASE_URL/scrobble/pause") {
            headers {
                appendTraktHeaders(accessToken)
                append("Content-Type", "application/json")
            }
            setBody(request)
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktScrobbleResponse>())
        } else {
            Result.failure(Exception("Failed to pause scrobble: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Stop scrobble (finished watching).
     */
    suspend fun stopScrobble(accessToken: String, request: TraktScrobbleRequest): Result<TraktScrobbleResponse> = try {
        val response = httpClient.post("$BASE_URL/scrobble/stop") {
            headers {
                appendTraktHeaders(accessToken)
                append("Content-Type", "application/json")
            }
            setBody(request)
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktScrobbleResponse>())
        } else {
            Result.failure(Exception("Failed to stop scrobble: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    /**
     * Get watched progress for a show.
     */
    suspend fun getShowProgress(accessToken: String, showId: String): Result<TraktProgressResponse> = try {
        val response = httpClient.get("$BASE_URL/shows/$showId/progress/watched") {
            headers {
                appendTraktHeaders(accessToken)
            }
        }
        
        if (response.status.isSuccess()) {
            Result.success(response.body<TraktProgressResponse>())
        } else {
            Result.failure(Exception("Failed to get show progress: ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    private fun HeadersBuilder.appendTraktHeaders(accessToken: String? = null) {
        append("trakt-api-version", API_VERSION)
        append("trakt-api-key", CLIENT_ID)
        append("Content-Type", "application/json")
        if (accessToken != null) {
            append("Authorization", "Bearer $accessToken")
        }
    }
}
