package com.nuvio.tv.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.DecoderPriority
import com.nuvio.tv.domain.model.PlayerPreference
import com.nuvio.tv.ui.theme.NuvioColors
import com.nuvio.tv.ui.viewmodel.PlaybackSettingsViewModel

@Composable
internal fun PlaybackSettingsPanel(
    viewModel: PlaybackSettingsViewModel
) {
    val preferences by viewModel.preferences.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SettingsDetailHeader("Playback", "Configure player, audio, and subtitle preferences")
        
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Player Section
                item {
                    Text(
                        text = "Player",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SettingsChoiceChip(
                            label = "Internal",
                            selected = preferences.preferredPlayer == PlayerPreference.INTERNAL,
                            onClick = { viewModel.setPreferredPlayer(PlayerPreference.INTERNAL) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "External",
                            selected = preferences.preferredPlayer == PlayerPreference.EXTERNAL,
                            onClick = { viewModel.setPreferredPlayer(PlayerPreference.EXTERNAL) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Text(
                        text = "Decoder Priority",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = NuvioColors.TextSecondary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsChoiceChip(
                            label = "Auto",
                            selected = preferences.decoderPriority == DecoderPriority.AUTO,
                            onClick = { viewModel.setDecoderPriority(DecoderPriority.AUTO) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Hardware",
                            selected = preferences.decoderPriority == DecoderPriority.HARDWARE,
                            onClick = { viewModel.setDecoderPriority(DecoderPriority.HARDWARE) },
                            modifier = Modifier.weight(1f)
                        )
                        SettingsChoiceChip(
                            label = "Software",
                            selected = preferences.decoderPriority == DecoderPriority.SOFTWARE,
                            onClick = { viewModel.setDecoderPriority(DecoderPriority.SOFTWARE) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Audio Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Audio",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "Audio Language",
                        subtitle = preferences.audioLanguage,
                        onClick = { /* TODO: Open language picker dialog */ }
                    )
                }

                // Subtitles Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Subtitles",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "Subtitle Language",
                        subtitle = preferences.subtitleLanguage,
                        onClick = { /* TODO: Open language picker dialog */ }
                    )
                }
                
                item {
                    SettingsSliderRow(
                        title = "Subtitle Size",
                        subtitle = "Adjust subtitle text size",
                        value = preferences.subtitleSize.toFloat(),
                        onValueChange = { viewModel.setSubtitleSize(it.toInt()) },
                        valueRange = 12f..32f,
                        steps = 19,
                        valueLabel = { "${it.toInt()}pt" }
                    )
                }

                // Behavior Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Behavior",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Auto-play Next Episode",
                        subtitle = "Automatically play next episode when current ends",
                        checked = preferences.autoPlayNextEpisode,
                        onCheckedChange = { viewModel.setAutoPlayNextEpisode(it) }
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Resume Playback",
                        subtitle = "Continue from where you left off",
                        checked = preferences.resumePlayback,
                        onCheckedChange = { viewModel.setResumePlayback(it) }
                    )
                }

                // Skip Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Skip",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Skip Intro",
                        subtitle = "Enable skip intro button during playback",
                        checked = preferences.skipIntroEnabled,
                        onCheckedChange = { viewModel.setSkipIntroEnabled(it) }
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Skip Outro",
                        subtitle = "Enable skip outro button during playback",
                        checked = preferences.skipOutroEnabled,
                        onCheckedChange = { viewModel.setSkipOutroEnabled(it) }
                    )
                }

                // Trailers Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Trailers",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsToggleRow(
                        title = "Auto-play Trailers",
                        subtitle = "Automatically play trailers on detail pages",
                        checked = preferences.trailerAutoPlay,
                        onCheckedChange = { viewModel.setTrailerAutoPlay(it) }
                    )
                }
            }
        }
    }
}
