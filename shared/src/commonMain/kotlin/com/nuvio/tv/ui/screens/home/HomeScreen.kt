package com.nuvio.tv.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.CatalogRow
import com.nuvio.tv.ui.components.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (String, String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        if (uiState.isLoading && uiState.catalogRows.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            }
        } else if (uiState.error != null && uiState.catalogRows.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(uiState.error!!, color = Color.White)
                Button(onClick = { /* Retry logic if any */ }) {
                    Text("Retry")
                }
            }
        } else {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (uiState.heroItems.isNotEmpty()) {
                    item {
                        HeroCarousel(
                            items = uiState.heroItems,
                            onItemClick = { item ->
                                onNavigateToDetail(item.id, item.apiType, "")
                            }
                        )
                    }
                }

                if (uiState.continueWatchingItems.isNotEmpty()) {
                    item {
                        ContinueWatchingSection(
                            items = uiState.continueWatchingItems,
                            onItemClick = { /* Handle click */ }
                        )
                    }
                }

                itemsIndexed(
                    items = uiState.catalogRows.filter { it.items.isNotEmpty() },
                    key = { _, row -> "${row.addonId}_${row.catalogId}" }
                ) { _, catalogRow ->
                    CatalogRowSection(
                        catalogRow = catalogRow,
                        onItemClick = onNavigateToDetail
                    )
                }
            }
        }
    }
}
