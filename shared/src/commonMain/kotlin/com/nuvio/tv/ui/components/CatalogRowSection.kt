package com.nuvio.tv.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.CatalogRow
import com.nuvio.tv.domain.model.MetaPreview

@Composable
fun CatalogRowSection(
    catalogRow: CatalogRow,
    onItemClick: (String, String, String) -> Unit,
    modifier: Modifier = Modifier,
    onSeeAll: () -> Unit = {},
    posterCardStyle: PosterCardStyle = PosterCardDefaults.Style,
    showPosterLabels: Boolean = true,
    showAddonName: Boolean = true,
    showCatalogTypeSuffix: Boolean = true,
    focusedPosterBackdropExpandEnabled: Boolean = false,
    focusedPosterBackdropExpandDelaySeconds: Int = 3,
    focusedPosterBackdropTrailerEnabled: Boolean = false,
    focusedPosterBackdropTrailerMuted: Boolean = true,
    trailerPreviewUrls: Map<String, String> = emptyMap(),
    onRequestTrailerPreview: (MetaPreview) -> Unit = {},
    onItemFocus: (MetaPreview) -> Unit = {},
    focusedItemIndex: Int = -1,
    onItemFocused: (itemIndex: Int) -> Unit = {},
    listState: LazyListState = rememberLazyListState()
) {
    val seeAllCardShape = RoundedCornerShape(posterCardStyle.cornerRadius)
    val currentOnItemFocused by rememberUpdatedState(onItemFocused)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                val catalogTitle = if (showCatalogTypeSuffix) {
                    "${catalogRow.catalogName.replaceFirstChar { it.uppercase() }} - ${catalogRow.apiType.replaceFirstChar { it.uppercase() }}"
                } else {
                    catalogRow.catalogName.replaceFirstChar { it.uppercase() }
                }
                Text(
                    text = catalogTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                if (showAddonName) {
                    Text(
                        text = "from ${catalogRow.addonName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = catalogRow.items,
                key = { index, item ->
                    "${catalogRow.addonId}_${catalogRow.type}_${catalogRow.catalogId}_${item.id}_$index"
                }
            ) { index, item ->
                ContentCard(
                    item = item,
                    posterCardStyle = posterCardStyle,
                    showLabels = showPosterLabels,
                    focusedPosterBackdropExpandEnabled = focusedPosterBackdropExpandEnabled,
                    focusedPosterBackdropExpandDelaySeconds = focusedPosterBackdropExpandDelaySeconds,
                    focusedPosterBackdropTrailerEnabled = focusedPosterBackdropTrailerEnabled,
                    focusedPosterBackdropTrailerMuted = focusedPosterBackdropTrailerMuted,
                    trailerPreviewUrl = trailerPreviewUrls[item.id],
                    onRequestTrailerPreview = onRequestTrailerPreview,
                    onFocus = onItemFocus,
                    onClick = { onItemClick(item.id, item.apiType, catalogRow.addonBaseUrl) },
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            currentOnItemFocused(index)
                        }
                    }
                )
            }

            if (catalogRow.items.size >= 15) {
                item {
                    Surface(
                        onClick = onSeeAll,
                        modifier = Modifier
                            .width(posterCardStyle.width)
                            .height(posterCardStyle.height),
                        shape = seeAllCardShape,
                        color = Color(0xFF1E1E1E)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "See All",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "See All",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
