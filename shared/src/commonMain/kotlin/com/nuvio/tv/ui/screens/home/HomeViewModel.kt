package com.nuvio.tv.ui.screens.home

import com.nuvio.tv.core.network.NetworkResult
import com.nuvio.tv.domain.model.CatalogRow
import com.nuvio.tv.domain.model.HomeLayout
import com.nuvio.tv.domain.model.MetaPreview
import com.nuvio.tv.domain.repository.AddonRepository
import com.nuvio.tv.domain.repository.CatalogRepository
import com.nuvio.tv.util.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

open class HomeViewModel(
    private val addonRepository: AddonRepository,
    private val catalogRepository: CatalogRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        coroutineScope.launch {
            addonRepository.getInstalledAddons().collect { addons ->
                loadHomeContent(addons)
            }
        }
    }

    private fun loadHomeContent(addons: List<com.nuvio.tv.domain.model.Addon>) {
        if (addons.isEmpty()) {
            _uiState.update { it.copy(isLoading = false, error = "No addons installed") }
            return
        }

        coroutineScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            addons.forEach { addon ->
                addon.catalogs.forEach { catalog ->
                    catalogRepository.getCatalog(
                        addonBaseUrl = addon.baseUrl,
                        addonId = addon.id,
                        addonName = addon.displayName,
                        catalogId = catalog.id,
                        catalogName = catalog.name,
                        type = catalog.apiType
                    ).collect { result ->
                        if (result is NetworkResult.Success) {
                            updateCatalogRow(result.data)
                            updateHeroItems(result.data.items)
                        }
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateCatalogRow(newRow: CatalogRow) {
        _uiState.update { state ->
            val updatedRows = state.catalogRows.toMutableList()
            val existingIndex = updatedRows.indexOfFirst { it.catalogId == newRow.catalogId && it.addonId == newRow.addonId }
            if (existingIndex != -1) {
                updatedRows[existingIndex] = newRow
            } else {
                updatedRows.add(newRow)
            }
            state.copy(catalogRows = updatedRows)
        }
    }

    private fun updateHeroItems(items: List<MetaPreview>) {
        if (items.isEmpty()) return
        _uiState.update { state ->
            val currentHero = state.heroItems.toMutableList()
            if (currentHero.size < 7) {
                val newItems = items.filter { it.background != null }.take(7 - currentHero.size)
                state.copy(heroItems = currentHero + newItems)
            } else state
        }
    }

    fun onEvent(event: HomeEvent) {
        // Handle basic events
    }
}
