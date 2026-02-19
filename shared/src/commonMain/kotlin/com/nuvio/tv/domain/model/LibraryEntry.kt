package com.nuvio.tv.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Represents a library entry (saved content item).
 * This can be from local storage or synced with Trakt.
 */
@Immutable
@Serializable
data class LibraryEntry(
    val id: String,
    val type: ContentType,
    val title: String,
    val year: String? = null,
    val poster: String? = null,
    val posterShape: PosterShape = PosterShape.POSTER,
    val background: String? = null,
    val logo: String? = null,
    val description: String? = null,
    val addonBaseUrl: String? = null,
    val addedAt: Long = System.currentTimeMillis(),
    val listKey: String? = null // For custom lists (null = default library/watchlist)
)

/**
 * Source mode for library data.
 */
enum class LibrarySourceMode {
    LOCAL,   // Local device storage only
    TRAKT    // Synced with Trakt.tv
}

/**
 * Content type tabs for library filtering.
 */
enum class LibraryTypeTab {
    MOVIES,
    SERIES
}

/**
 * List tabs for library organization.
 */
@Serializable
data class LibraryListTab(
    val key: String,
    val name: String,
    val isCustom: Boolean = false
) {
    companion object {
        val WATCHLIST = LibraryListTab("watchlist", "Watchlist", false)
        val COLLECTION = LibraryListTab("collection", "Collection", false)
    }
}
