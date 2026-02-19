package com.nuvio.tv.domain.model

data class IntegrationPreferences(
    // TMDB Enrichment
    val tmdbEnabled: Boolean = false,
    val tmdbLanguage: String = "en",
    val tmdbDescriptions: Boolean = true,
    val tmdbRatings: Boolean = true,
    val tmdbPosters: Boolean = true,
    val tmdbBackdrops: Boolean = true,
    val tmdbLogos: Boolean = true,
    
    // MDBList
    val mdbListApiKey: String = "",
    val mdbListEnabled: Boolean = false,
    
    // Real-Debrid
    val realDebridApiKey: String = "",
    val realDebridEnabled: Boolean = false
)
