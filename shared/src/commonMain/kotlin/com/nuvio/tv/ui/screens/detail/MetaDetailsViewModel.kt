package com.nuvio.tv.ui.screens.detail

import com.nuvio.tv.core.network.NetworkResult
import com.nuvio.tv.domain.model.Video
import com.nuvio.tv.domain.repository.MetaRepository
import com.nuvio.tv.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MetaDetailsViewModel(
    private val metaRepository: MetaRepository,
    private val coroutineScope: CoroutineScope,
    private val itemId: String,
    private val itemType: String,
    private val preferredAddonBaseUrl: String? = null
) {
    private val _uiState = MutableStateFlow(MetaDetailsUiState())
    val uiState: StateFlow<MetaDetailsUiState> = _uiState.asStateFlow()

    // Callbacks for navigation
    var onNavigateToStream: ((String, String, String, Int?, Int?) -> Unit)? = null
    var onNavigateBack: (() -> Unit)? = null

    init {
        loadMeta()
    }

    fun onEvent(event: MetaDetailsEvent) {
        when (event) {
            is MetaDetailsEvent.OnSeasonSelected -> {
                selectSeason(event.season)
            }
            is MetaDetailsEvent.OnEpisodeClick -> {
                playEpisode(event.video)
            }
            MetaDetailsEvent.OnPlayClick -> {
                playContent()
            }
            MetaDetailsEvent.OnRetry -> {
                loadMeta()
            }
            MetaDetailsEvent.OnBackPress -> {
                onNavigateBack?.invoke()
            }
        }
    }

    private fun loadMeta() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        coroutineScope.launch {
            val flow = if (preferredAddonBaseUrl != null) {
                metaRepository.getMeta(preferredAddonBaseUrl, itemType, itemId)
            } else {
                metaRepository.getMetaFromAllAddons(itemType, itemId)
            }

            flow.collectLatest { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val meta = result.data
                        val seasons = extractSeasons(meta.videos)
                        val selectedSeason = seasons.firstOrNull() ?: 1
                        val episodes = filterEpisodesForSeason(meta.videos, selectedSeason)
                        
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                meta = meta,
                                error = null,
                                seasons = seasons,
                                selectedSeason = selectedSeason,
                                episodesForSeason = episodes
                            )
                        }
                        Logger.d("MetaDetailsViewModel", "Meta loaded successfully: ${meta.name}")
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Logger.e("MetaDetailsViewModel", "Failed to load meta: ${result.message}", null)
                    }
                    NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun selectSeason(season: Int) {
        val meta = _uiState.value.meta ?: return
        val episodes = filterEpisodesForSeason(meta.videos, season)
        _uiState.update {
            it.copy(
                selectedSeason = season,
                episodesForSeason = episodes
            )
        }
    }

    private fun playContent() {
        val meta = _uiState.value.meta ?: return
        
        // For movies, navigate directly to stream selection
        // For series, play the first episode of first season (or next to watch logic later)
        if (meta.videos.isEmpty()) {
            // Movie
            onNavigateToStream?.invoke(itemId, itemType, meta.name, null, null)
        } else {
            // Series - play first episode
            val firstEpisode = _uiState.value.episodesForSeason.firstOrNull()
            if (firstEpisode != null) {
                playEpisode(firstEpisode)
            }
        }
    }

    private fun playEpisode(video: Video) {
        val meta = _uiState.value.meta ?: return
        onNavigateToStream?.invoke(
            video.id,
            itemType,
            "${meta.name} - ${video.title}",
            video.season,
            video.episode
        )
    }

    private fun extractSeasons(videos: List<Video>): List<Int> {
        return videos.mapNotNull { it.season }.distinct().sorted()
    }

    private fun filterEpisodesForSeason(videos: List<Video>, season: Int): List<Video> {
        return videos.filter { it.season == season }.sortedBy { it.episode }
    }
}
