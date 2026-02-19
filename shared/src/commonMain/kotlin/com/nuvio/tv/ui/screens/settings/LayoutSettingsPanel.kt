package com.nuvio.tv.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.HomeLayout
import com.nuvio.tv.ui.viewmodel.LayoutSettingsViewModel

@Composable
internal fun LayoutSettingsPanel(
    viewModel: LayoutSettingsViewModel
) {
    val preferences by viewModel.preferences.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SettingsDetailHeader("Layout", "Customize your home screen layout and appearance")
        
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Home Layout Section
                item {
                    Text(
                        text = "Home Layout",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = com.nuvio.tv.ui.theme.NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SettingsChoiceChip(
                            label = "Classic",
                            selected = preferences.homeLayout == HomeLayout.CLASSIC,
                            onClick = { viewModel.selectLayout(HomeLayout.CLASSIC) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Grid",
                            selected = preferences.homeLayout == HomeLayout.GRID,
                            onClick = { viewModel.selectLayout(HomeLayout.GRID) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Home Content Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Home Content",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = com.nuvio.tv.ui.theme.NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Hero Section",
                        subtitle = "Show featured content at the top of home",
                        checked = preferences.heroSectionEnabled,
                        onCheckedChange = { viewModel.setHeroSectionEnabled(it) }
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Poster Labels",
                        subtitle = "Display titles below content posters",
                        checked = preferences.posterLabelsEnabled,
                        onCheckedChange = { viewModel.setPosterLabelsEnabled(it) }
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Addon Names",
                        subtitle = "Show addon source in catalog rows",
                        checked = preferences.catalogAddonNameEnabled,
                        onCheckedChange = { viewModel.setCatalogAddonNameEnabled(it) }
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Continue Watching",
                        subtitle = "Show recently watched content",
                        checked = preferences.continueWatchingEnabled,
                        onCheckedChange = { viewModel.setContinueWatchingEnabled(it) }
                    )
                }

                // Detail Page Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Detail Page",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = com.nuvio.tv.ui.theme.NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Trailer Button",
                        subtitle = "Enable trailer button on detail pages",
                        checked = preferences.detailPageTrailerButtonEnabled,
                        onCheckedChange = { viewModel.setDetailPageTrailerButtonEnabled(it) }
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Prefer External Metadata",
                        subtitle = "Use external addon for detail page metadata",
                        checked = preferences.preferExternalMetaAddonDetail,
                        onCheckedChange = { viewModel.setPreferExternalMetaAddonDetail(it) }
                    )
                }

                // Focused Poster Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Focused Poster",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = com.nuvio.tv.ui.theme.NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsSliderRow(
                        title = "Expand Delay",
                        subtitle = "Seconds before focused poster expands",
                        value = preferences.focusedPosterDelaySeconds.toFloat(),
                        onValueChange = { viewModel.setFocusedPosterDelaySeconds(it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 8,
                        valueLabel = { "${it.toInt()}s" }
                    )
                }

                // Poster Card Style Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Poster Card Style",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = com.nuvio.tv.ui.theme.NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsChoiceChip(
                            label = "Small",
                            selected = preferences.posterCardWidthDp == 105,
                            onClick = { viewModel.setPosterCardWidth(105) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Medium",
                            selected = preferences.posterCardWidthDp == 126,
                            onClick = { viewModel.setPosterCardWidth(126) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Large",
                            selected = preferences.posterCardWidthDp == 147,
                            onClick = { viewModel.setPosterCardWidth(147) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsChoiceChip(
                            label = "Round",
                            selected = preferences.posterCardCornerRadiusDp == 8,
                            onClick = { viewModel.setPosterCardCornerRadius(8) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Rounded",
                            selected = preferences.posterCardCornerRadiusDp == 12,
                            onClick = { viewModel.setPosterCardCornerRadius(12) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Sharp",
                            selected = preferences.posterCardCornerRadiusDp == 4,
                            onClick = { viewModel.setPosterCardCornerRadius(4) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    SettingsButton(
                        text = "Reset to Default",
                        onClick = { viewModel.resetPosterCardStyle() },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
