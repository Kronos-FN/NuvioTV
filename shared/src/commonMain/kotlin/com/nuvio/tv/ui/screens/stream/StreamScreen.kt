package com.nuvio.tv.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.Stream
import com.nuvio.tv.ui.components.EmptyScreenState
import com.nuvio.tv.ui.components.LoadingIndicator
import com.nuvio.tv.ui.components.NetworkImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreamScreen(
    viewModel: StreamScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (uiState.season != null && uiState.episode != null) {
                            Text(
                                text = "S${uiState.season} E${uiState.episode}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(StreamScreenEvent.OnBackPress) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                uiState.error != null -> {
                    EmptyScreenState(
                        title = uiState.error ?: "Failed to load streams"
                    )
                }
                uiState.filteredStreams.isEmpty() -> {
                    EmptyScreenState(
                        title = "No streams found"
                    )
                }
                else -> {
                    StreamListContent(
                        uiState = uiState,
                        onAddonFilterSelected = { addon ->
                            viewModel.onEvent(StreamScreenEvent.OnAddonFilterSelected(addon))
                        },
                        onStreamSelected = { stream ->
                            viewModel.onEvent(StreamScreenEvent.OnStreamSelected(stream))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StreamListContent(
    uiState: StreamScreenUiState,
    onAddonFilterSelected: (String?) -> Unit,
    onStreamSelected: (Stream) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Addon filter chips
        if (uiState.availableAddons.size > 1) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Filter by addon",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = uiState.selectedAddonFilter == null,
                                onClick = { onAddonFilterSelected(null) },
                                label = { Text("All (${uiState.allStreams.size})") }
                            )
                        }
                        
                        items(uiState.availableAddons) { addonName ->
                            val count = uiState.allStreams.count { it.addonName == addonName }
                            FilterChip(
                                selected = uiState.selectedAddonFilter == addonName,
                                onClick = { onAddonFilterSelected(addonName) },
                                label = { Text("$addonName ($count)") }
                            )
                        }
                    }
                }
            }
        }
        
        // Stream count
        item {
            Text(
                text = "${uiState.filteredStreams.size} streams available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        // Streams list
        items(uiState.filteredStreams) { stream ->
            StreamCard(
                stream = stream,
                onClick = { onStreamSelected(stream) }
            )
        }
    }
}

@Composable
private fun StreamCard(
    stream: Stream,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Addon logo or initial
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (stream.addonLogo != null) {
                    NetworkImage(
                        url = stream.addonLogo,
                        contentDescription = stream.addonName,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Text(
                        text = stream.addonName.firstOrNull()?.toString()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Stream info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Stream name
                Text(
                    text = stream.getDisplayName(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Addon name
                Text(
                    text = stream.addonName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Stream description
                stream.getDisplayDescription()?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Stream type indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (stream.isTorrent()) {
                        StreamTypeBadge("Torrent", MaterialTheme.colorScheme.tertiary)
                    }
                    if (stream.isYouTube()) {
                        StreamTypeBadge("YouTube", Color(0xFFFF0000))
                    }
                    if (stream.isExternal()) {
                        StreamTypeBadge("External", MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            
            // Play icon
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun StreamTypeBadge(
    label: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
