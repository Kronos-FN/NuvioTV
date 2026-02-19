package com.nuvio.tv.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuvio.tv.domain.model.ContentType
import com.nuvio.tv.domain.model.LibraryEntry
import com.nuvio.tv.domain.model.LibraryTypeTab
import com.nuvio.tv.ui.components.EmptyScreenState
import com.nuvio.tv.ui.components.NetworkImage

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onNavigateToDetail: (String, ContentType, String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Set navigation callback
    LaunchedEffect(Unit) {
        viewModel.onNavigateToDetail = { id, type, baseUrl ->
            onNavigateToDetail(id, type, baseUrl)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        // Header with title and source mode
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Source mode indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier.clickable { 
                        viewModel.handleEvent(LibraryEvent.OnSwitchSourceMode) 
                    }
                ) {
                    Text(
                        text = uiState.sourceMode.name,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = if (uiState.sourceMode == com.nuvio.tv.domain.model.LibrarySourceMode.TRAKT) 
                            Color(0xFFE91E63) else Color.White,
                        fontSize = 14.sp
                    )
                }
                
                // Manage Lists button
                IconButton(onClick = { 
                    viewModel.handleEvent(LibraryEvent.OnToggleManageListsDialog) 
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Manage Lists", tint = Color.White)
                }
            }
        }

        // Content Type Tabs
        TabRow(
            selectedTabIndex = when (uiState.selectedTypeTab) {
                LibraryTypeTab.MOVIES -> 0
                LibraryTypeTab.SERIES -> 1
            },
            containerColor = Color.Transparent,
            contentColor = Color(0xFFE91E63),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Tab(
                selected = uiState.selectedTypeTab == LibraryTypeTab.MOVIES,
                onClick = { viewModel.handleEvent(LibraryEvent.OnTypeTabChange(LibraryTypeTab.MOVIES)) },
                text = { Text("Movies") }
            )
            Tab(
                selected = uiState.selectedTypeTab == LibraryTypeTab.SERIES,
                onClick = { viewModel.handleEvent(LibraryEvent.OnTypeTabChange(LibraryTypeTab.SERIES)) },
                text = { Text("Series") }
            )
        }

        // List Selector
        ScrollableTabRow(
            selectedTabIndex = uiState.availableLists.indexOfFirst { it.key == uiState.selectedListKey }.let { if (it == -1) 0 else it },
            containerColor = Color.Transparent,
            contentColor = Color(0xFFE91E63),
            edgePadding = 0.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Tab(
                selected = uiState.selectedListKey == null,
                onClick = { viewModel.handleEvent(LibraryEvent.OnListTabChange(null)) },
                text = { Text("All") }
            )
            uiState.availableLists.forEach { listTab ->
                Tab(
                    selected = uiState.selectedListKey == listTab.key,
                    onClick = { viewModel.handleEvent(LibraryEvent.OnListTabChange(listTab.key)) },
                    text = { Text(listTab.name) }
                )
            }
        }

        // Content
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE91E63))
                }
            }
            uiState.error != null -> {
                EmptyScreenState(
                    title = "Error",
                    subtitle = uiState.error ?: "Unknown error"
                )
            }
            uiState.visibleItems.isEmpty() -> {
                val emptyMessage = when (uiState.selectedTypeTab) {
                    LibraryTypeTab.MOVIES -> "No movies in your library yet.\nStart saving your favorites!"
                    LibraryTypeTab.SERIES -> "No series in your library yet.\nStart saving your favorites!"
                }
                EmptyScreenState(
                    title = "Empty Library",
                    subtitle = emptyMessage
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.visibleItems, key = { it.id + (it.listKey ?: "") }) { entry ->
                        LibraryItemCard(
                            entry = entry,
                            onClick = { viewModel.handleEvent(LibraryEvent.OnItemClick(entry)) },
                            onRemove = { viewModel.handleEvent(LibraryEvent.OnRemoveItem(entry)) }
                        )
                    }
                }
            }
        }
    }

    // Manage Lists Dialog
    if (uiState.isManageListsDialogOpen) {
        ManageListsDialog(
            lists = uiState.availableLists.filter { it.isCustom },
            isAddingList = uiState.isAddingList,
            onDismiss = { viewModel.handleEvent(LibraryEvent.OnToggleManageListsDialog) },
            onCreateList = { name -> viewModel.handleEvent(LibraryEvent.OnCreateList(name)) },
            onDeleteList = { key -> viewModel.handleEvent(LibraryEvent.OnDeleteList(key)) }
        )
    }
}

@Composable
private fun LibraryItemCard(
    entry: LibraryEntry,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    var showRemoveButton by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(entry.posterShape.aspectRatio())
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Poster image
            if (entry.poster != null) {
                NetworkImage(
                    url = entry.poster,
                    contentDescription = entry.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF2E2E2E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = entry.title.take(1),
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Title overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(8.dp)
            ) {
                Text(
                    text = entry.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Remove button (shows on hover/long press)
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove from library",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun ManageListsDialog(
    lists: List<com.nuvio.tv.domain.model.LibraryListTab>,
    isAddingList: Boolean,
    onDismiss: () -> Unit,
    onCreateList: (String) -> Unit,
    onDeleteList: (String) -> Unit
) {
    var newListName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Lists") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Create new list
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("New list name") },
                        singleLine = true,
                        enabled = !isAddingList
                    )
                    IconButton(
                        onClick = {
                            if (newListName.isNotBlank()) {
                                onCreateList(newListName)
                                newListName = ""
                            }
                        },
                        enabled = !isAddingList && newListName.isNotBlank()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create list")
                    }
                }

                HorizontalDivider()

                // Existing custom lists
                lists.forEach { list ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(list.name)
                        IconButton(onClick = { onDeleteList(list.key) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete list", tint = Color(0xFFE91E63))
                        }
                    }
                }

                if (lists.isEmpty()) {
                    Text(
                        "No custom lists yet",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
