package com.nuvio.tv.domain.model

data class PlayerPreferences(
    val preferredPlayer: PlayerPreference = PlayerPreference.INTERNAL,
    val audioLanguage: String = "en",
    val subtitleLanguage: String = "en",
    val subtitleSize: Int = 16,
    val subtitleStyle: SubtitleStyle = SubtitleStyle(),
    val decoderPriority: DecoderPriority = DecoderPriority.AUTO,
    val autoPlayNextEpisode: Boolean = true,
    val resumePlayback: Boolean = true,
    val skipIntroEnabled: Boolean = true,
    val skipOutroEnabled: Boolean = false,
    val trailerAutoPlay: Boolean = false
)
