package com.nuvio.tv.ui.screens.library

import androidx.compose.runtime.Immutable
import com.nuvio.tv.domain.model.LibraryEntry
import com.nuvio.tv.domain.model.LibraryListTab
import com.nuvio.tv.domain.model.LibrarySourceMode
import com.nuvio.tv.domain.model.LibraryTypeTab

@Immutable
data class LibraryUiState(
    val visibleItems: List<LibraryEntry> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val sourceMode: LibrarySourceMode = LibrarySourceMode.LOCAL,
    val selectedTypeTab: LibraryTypeTab = LibraryTypeTab.MOVIES,
    val selectedListKey: String? = null,
    val availableLists: List<LibraryListTab> = listOf(
        LibraryListTab.WATCHLIST,
        LibraryListTab.COLLECTION
    ),
    val isManageListsDialogOpen: Boolean = false,
    val isAddingList: Boolean = false
)

sealed class LibraryEvent {
    data class OnTypeTabChange(val tab: LibraryTypeTab) : LibraryEvent()
    data class OnListTabChange(val listKey: String?) : LibraryEvent()
    data class OnItemClick(val entry: LibraryEntry) : LibraryEvent()
    data class OnRemoveItem(val entry: LibraryEntry) : LibraryEvent()
    data object OnToggleManageListsDialog : LibraryEvent()
    data class OnCreateList(val name: String) : LibraryEvent()
    data class OnDeleteList(val key: String) : LibraryEvent()
    data class OnRenameList(val key: String, val newName: String) : LibraryEvent()
    data object OnSwitchSourceMode : LibraryEvent()
    data object OnRetry : LibraryEvent()
}
