package com.nuvio.tv.ui.screens.library

import com.nuvio.tv.domain.model.ContentType
import com.nuvio.tv.domain.model.LibraryListTab
import com.nuvio.tv.domain.model.LibraryTypeTab
import com.nuvio.tv.domain.repository.LibraryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val libraryRepository: LibraryRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()
    
    var onNavigateToDetail: ((id: String, type: ContentType, baseUrl: String?) -> Unit)? = null

    init {
        loadLibraryItems()
        loadCustomLists()
    }

    private fun loadLibraryItems() {
        coroutineScope.launch {
            combine(
                libraryRepository.getLibraryItems(),
                _uiState.map { it.selectedTypeTab },
                _uiState.map { it.selectedListKey }
            ) { items, typeTab, listKey ->
                val contentType = when (typeTab) {
                    LibraryTypeTab.MOVIES -> ContentType.MOVIE
                    LibraryTypeTab.SERIES -> ContentType.SERIES
                }
                items.filter { entry ->
                    val typeMatches = entry.type == contentType
                    val listMatches = listKey == null || entry.listKey == listKey
                    typeMatches && listMatches
                }
            }.collect { filteredItems ->
                _uiState.update { it.copy(
                    visibleItems = filteredItems,
                    isLoading = false
                ) }
            }
        }
    }

    private fun loadCustomLists() {
        coroutineScope.launch {
            libraryRepository.getCustomLists().collect { customLists ->
                _uiState.update { state ->
                    val baseListsExisted = listOf(
                        LibraryListTab.WATCHLIST,
                        LibraryListTab.COLLECTION
                    )
                    state.copy(availableLists = baseListsExisted + customLists)
                }
            }
        }
    }

    fun handleEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.OnTypeTabChange -> {
                _uiState.update { it.copy(selectedTypeTab = event.tab) }
            }
            is LibraryEvent.OnListTabChange -> {
                _uiState.update { it.copy(selectedListKey = event.listKey) }
            }
            is LibraryEvent.OnItemClick -> {
                onNavigateToDetail?.invoke(
                    event.entry.id,
                    event.entry.type,
                    event.entry.addonBaseUrl
                )
            }
            is LibraryEvent.OnRemoveItem -> {
                coroutineScope.launch {
                    libraryRepository.removeFromLibrary(
                        event.entry.id,
                        event.entry.listKey
                    )
                }
            }
            is LibraryEvent.OnToggleManageListsDialog -> {
                _uiState.update { it.copy(
                    isManageListsDialogOpen = !it.isManageListsDialogOpen
                ) }
            }
            is LibraryEvent.OnCreateList -> {
                coroutineScope.launch {
                    _uiState.update { it.copy(isAddingList = true) }
                    libraryRepository.createCustomList(event.name)
                    _uiState.update { it.copy(isAddingList = false) }
                }
            }
            is LibraryEvent.OnDeleteList -> {
                coroutineScope.launch {
                    libraryRepository.deleteCustomList(event.key)
                }
            }
            is LibraryEvent.OnRenameList -> {
                coroutineScope.launch {
                    libraryRepository.renameCustomList(event.key, event.newName)
                }
            }
            is LibraryEvent.OnSwitchSourceMode -> {
                val newMode = when (_uiState.value.sourceMode) {
                    com.nuvio.tv.domain.model.LibrarySourceMode.LOCAL -> com.nuvio.tv.domain.model.LibrarySourceMode.TRAKT
                    com.nuvio.tv.domain.model.LibrarySourceMode.TRAKT -> com.nuvio.tv.domain.model.LibrarySourceMode.LOCAL
                }
                _uiState.update { it.copy(sourceMode = newMode) }
                // TODO: Implement Trakt sync when Trakt integration is complete
            }
            is LibraryEvent.OnRetry -> {
                _uiState.update { it.copy(isLoading = true, error = null) }
                loadLibraryItems()
            }
        }
    }
}
