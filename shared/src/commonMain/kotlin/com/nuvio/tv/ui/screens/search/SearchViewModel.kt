package com.nuvio.tv.ui.screens.search

import com.nuvio.tv.core.network.NetworkResult
import com.nuvio.tv.domain.model.Addon
import com.nuvio.tv.domain.model.CatalogDescriptor
import com.nuvio.tv.domain.model.CatalogRow
import com.nuvio.tv.domain.repository.AddonRepository
import com.nuvio.tv.domain.repository.CatalogRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

open class SearchViewModel(
    private val addonRepository: AddonRepository,
    private val catalogRepository: CatalogRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val catalogsMap = mutableMapOf<String, CatalogRow>()
    private val catalogOrder = mutableListOf<String>()

    private var activeSearchJobs: List<Job> = emptyList()
    private var discoverJob: Job? = null
    private var catalogRowsUpdateJob: Job? = null
    private var pendingCatalogResponses = 0

    init {
        coroutineScope.launch {
            loadDiscoverCatalogs()
        }
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> onQueryChanged(event.query)
            SearchEvent.SubmitSearch -> submitSearch()
            is SearchEvent.LoadMoreCatalog -> {} 
            is SearchEvent.SelectDiscoverType -> {}
            is SearchEvent.SelectDiscoverCatalog -> {}
            is SearchEvent.SelectDiscoverGenre -> {}
            SearchEvent.ShowMoreDiscoverResults -> {}
            SearchEvent.LoadMoreDiscoverResults -> {}
            SearchEvent.Retry -> performSearch(_uiState.value.submittedQuery)
        }
    }

    private fun onQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                error = null,
                isSearching = false
            )
        }
        activeSearchJobs.forEach { it.cancel() }
    }

    private fun submitSearch() {
        performSearch(_uiState.value.query)
    }

    private fun performSearch(query: String) {
        val trimmed = query.trim()
        _uiState.update { it.copy(submittedQuery = trimmed, isSearching = true, error = null) }
        
        activeSearchJobs.forEach { it.cancel() }
        catalogsMap.clear()
        catalogOrder.clear()

        if (trimmed.length < 2) {
            _uiState.update { it.copy(isSearching = false, catalogRows = emptyList()) }
            return
        }

        coroutineScope.launch {
            val addons = addonRepository.getInstalledAddons().first()
            val targets = addons.flatMap { addon ->
                addon.catalogs.filter { it.extra.any { e -> e.name == "search" } }
                    .map { addon to it }
            }

            if (targets.isEmpty()) {
                _uiState.update { it.copy(isSearching = false, error = "No searchable catalogs") }
                return@launch
            }

            activeSearchJobs = targets.map { (addon, catalog) ->
                launch {
                    val key = "${addon.id}_${catalog.apiType}_${catalog.id}"
                    catalogOrder.add(key)
                    catalogRepository.getCatalog(
                        addonBaseUrl = addon.baseUrl,
                        addonId = addon.id,
                        addonName = addon.displayName,
                        catalogId = catalog.id,
                        catalogName = catalog.name,
                        type = catalog.apiType,
                        extraArgs = mapOf("search" to trimmed)
                    ).collect { result ->
                        if (result is NetworkResult.Success) {
                            catalogsMap[key] = result.data
                            updateCatalogRows()
                        }
                    }
                }
            }
        }
    }

    private fun updateCatalogRows() {
        _uiState.update { state ->
            state.copy(catalogRows = catalogOrder.mapNotNull { catalogsMap[it] })
        }
    }

    private suspend fun loadDiscoverCatalogs() {
        // Basic impl
    }
}
