package com.nuvio.tv.ui.screens.stream

import com.nuvio.tv.core.network.NetworkResult
import com.nuvio.tv.domain.model.Stream
import com.nuvio.tv.domain.repository.StreamRepository
import com.nuvio.tv.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StreamScreenViewModel(
    private val streamRepository: StreamRepository,
    private val coroutineScope: CoroutineScope,
    private val videoId: String,
    private val contentType: String,
    private val title: String,
    private val season: Int? = null,
    private val episode: Int? = null
) {
    private val _uiState = MutableStateFlow(
        StreamScreenUiState(
            videoId = videoId,
            contentType = contentType,
            title = title,
            season = season,
            episode = episode
        )
    )
    val uiState: StateFlow<StreamScreenUiState> = _uiState.asStateFlow()

    // Callbacks for navigation
    var onNavigateToPlayer: ((String, String, Map<String, String>?) -> Unit)? = null
    var onNavigateBack: (() -> Unit)? = null

    init {
        loadStreams()
    }

    fun onEvent(event: StreamScreenEvent) {
        when (event) {
            is StreamScreenEvent.OnAddonFilterSelected -> {
                filterByAddon(event.addonName)
            }
            is StreamScreenEvent.OnStreamSelected -> {
                playStream(event.stream)
            }
            StreamScreenEvent.OnRetry -> {
                loadStreams()
            }
            StreamScreenEvent.OnBackPress -> {
                onNavigateBack?.invoke()
            }
        }
    }

    private fun loadStreams() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        coroutineScope.launch {
            streamRepository.getStreamsFromAllAddons(
                type = contentType,
                videoId = videoId,
                season = season,
                episode = episode
            ).collectLatest { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val addonStreams = result.data
                        val allStreams = addonStreams.flatMap { it.streams }
                        val availableAddons = addonStreams.map { it.addonName }
                        
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                addonStreams = addonStreams,
                                allStreams = allStreams,
                                filteredStreams = allStreams,
                                availableAddons = availableAddons,
                                error = null
                            )
                        }
                        Logger.d("StreamScreenViewModel", "Loaded ${allStreams.size} streams from ${availableAddons.size} addons")
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        Logger.e("StreamScreenViewModel", "Failed to load streams: ${result.message}", null)
                    }
                    NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun filterByAddon(addonName: String?) {
        val filtered = if (addonName == null) {
            _uiState.value.allStreams
        } else {
            _uiState.value.allStreams.filter { it.addonName == addonName }
        }
        
        _uiState.update {
            it.copy(
                selectedAddonFilter = addonName,
                filteredStreams = filtered
            )
        }
    }

    private fun playStream(stream: Stream) {
        val streamUrl = stream.getStreamUrl()
        if (streamUrl != null) {
            val headers = stream.behaviorHints?.proxyHeaders?.request
            onNavigateToPlayer?.invoke(streamUrl, _uiState.value.title, headers)
        } else {
            Logger.w("StreamScreenViewModel", "Stream has no URL: ${stream.getDisplayName()}")
        }
    }
}
