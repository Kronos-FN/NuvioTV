package com.nuvio.tv.ui.screens.search

sealed class SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent()
    object SubmitSearch : SearchEvent()
    data class LoadMoreCatalog(val catalogId: String, val addonId: String, val type: String) : SearchEvent()
    data class SelectDiscoverType(val type: String) : SearchEvent()
    data class SelectDiscoverCatalog(val catalogKey: String) : SearchEvent()
    data class SelectDiscoverGenre(val genre: String?) : SearchEvent()
    object ShowMoreDiscoverResults : SearchEvent()
    object LoadMoreDiscoverResults : SearchEvent()
    object Retry : SearchEvent()
}
