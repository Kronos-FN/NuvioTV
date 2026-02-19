package com.nuvio.tv.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nuvio.tv.ui.theme.NuvioColors
import com.nuvio.tv.ui.viewmodel.IntegrationSettingsViewModel

@Composable
internal fun IntegrationSettingsPanel(
    viewModel: IntegrationSettingsViewModel
) {
    val preferences by viewModel.preferences.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SettingsDetailHeader("Integration", "Configure external services and metadata sources")
        
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Debrid Services Section
                item {
                    Text(
                        text = "Debrid Services",
                        style = MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "Real-Debrid",
                        subtitle = if (preferences.realDebridEnabled) "Connected" else "Not connected",
                        onClick = { /* TODO: Open Real-Debrid connection dialog */ }
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "AllDebrid",
                        subtitle = "Coming soon",
                        onClick = { }
                    )
                }

                // TMDB Enrichment Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "TMDB Enrichment",
                        style = MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Enable TMDB",
                        subtitle = "Use The Movie Database for metadata enrichment",
                        checked = preferences.tmdbEnabled,
                        onCheckedChange = { viewModel.setTmdbEnabled(it) }
                    )
                }
                
                if (preferences.tmdbEnabled) {
                    item {
                        SettingsActionRow(
                            title = "Language",
                            subtitle = preferences.tmdbLanguage,
                            onClick = { /* TODO: Open language picker */ }
                        )
                    }
                    
                    item {
                        Text(
                            text = "Metadata Options",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NuvioColors.TextSecondary,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )
                    }
                    
                    item {
                        SettingsToggleRow(
                            title = "Descriptions",
                            subtitle = "Use TMDB descriptions and summaries",
                            checked = preferences.tmdbDescriptions,
                            onCheckedChange = { viewModel.setTmdbDescriptions(it) }
                        )
                    }
                    
                    item {
                        SettingsToggleRow(
                            title = "Ratings",
                            subtitle = "Show TMDB ratings",
                            checked = preferences.tmdbRatings,
                            onCheckedChange = { viewModel.setTmdbRatings(it) }
                        )
                    }
                    
                    item {
                        SettingsToggleRow(
                            title = "Posters",
                            subtitle = "Use TMDB poster images",
                            checked = preferences.tmdbPosters,
                            onCheckedChange = { viewModel.setTmdbPosters(it) }
                        )
                    }
                    
                    item {
                        SettingsToggleRow(
                            title = "Backdrops",
                            subtitle = "Use TMDB backdrop images",
                            checked = preferences.tmdbBackdrops,
                            onCheckedChange = { viewModel.setTmdbBackdrops(it) }
                        )
                    }
                    
                    item {
                        SettingsToggleRow(
                            title = "Logos",
                            subtitle = "Use TMDB logo images",
                            checked = preferences.tmdbLogos,
                            onCheckedChange = { viewModel.setTmdbLogos(it) }
                        )
                    }
                }

                // MDBList Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "MDBList Ratings",
                        style = MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Enable MDBList",
                        subtitle = "Show aggregated ratings from multiple sources",
                        checked = preferences.mdbListEnabled,
                        onCheckedChange = { viewModel.setMdbListEnabled(it) }
                    )
                }
                
                if (preferences.mdbListEnabled) {
                    item {
                        SettingsActionRow(
                            title = "API Key",
                            subtitle = if (preferences.mdbListApiKey.isNotBlank()) "Configured" else "Not set",
                            onClick = { /* TODO: Open API key input dialog */ }
                        )
                    }
                }
            }
        }
    }
}
