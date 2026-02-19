package com.nuvio.tv.ui.screens.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuvio.tv.data.preferences.*
import com.nuvio.tv.domain.model.AppTheme
import com.nuvio.tv.ui.theme.NuvioColors
import com.nuvio.tv.ui.theme.NuvioTheme
import com.nuvio.tv.ui.theme.ThemeColors
import com.nuvio.tv.ui.viewmodel.*

internal enum class SettingsCategory {
    ACCOUNT, APPEARANCE, LAYOUT, PLUGINS, INTEGRATION, PLAYBACK, TRAKT, ABOUT
}

@Composable
fun SettingsScreen(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(SettingsCategory.APPEARANCE) }

    // Create stores and ViewModels
    val layoutStore = remember { LayoutPreferencesStore() }
    val playerStore = remember { PlayerPreferencesStore() }
    val traktStore = remember { TraktPreferencesStore() }
    val integrationStore = remember { IntegrationPreferencesStore() }
    
    val traktApi = remember {
        com.nuvio.tv.data.remote.api.TraktApi(
            io.ktor.client.HttpClient()
        )
    }

    val layoutViewModel = remember { LayoutSettingsViewModel(layoutStore) }
    val playbackViewModel = remember { PlaybackSettingsViewModel(playerStore) }
    val traktViewModel = remember(traktApi) {
        TraktViewModel(traktStore, traktApi)
    }
    val integrationViewModel = remember { IntegrationSettingsViewModel(integrationStore) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NuvioColors.Background)
            .padding(32.dp)
    ) {
        SettingsWorkspaceSurface(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sidebar Rail
                LazyColumn(
                    modifier = Modifier.width(282.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
                ) {
                    item { SettingsRailButton("Account", selectedCategory == SettingsCategory.ACCOUNT, { selectedCategory = SettingsCategory.ACCOUNT }, icon = Icons.Default.Person) }
                    item { SettingsRailButton("Appearance", selectedCategory == SettingsCategory.APPEARANCE, { selectedCategory = SettingsCategory.APPEARANCE }, icon = Icons.Default.Edit) }
                    item { SettingsRailButton("Layout", selectedCategory == SettingsCategory.LAYOUT, { selectedCategory = SettingsCategory.LAYOUT }, icon = Icons.Default.Menu) }
                    item { SettingsRailButton("Plugins", selectedCategory == SettingsCategory.PLUGINS, { selectedCategory = SettingsCategory.PLUGINS }, icon = Icons.Default.Build) }
                    item { SettingsRailButton("Integration", selectedCategory == SettingsCategory.INTEGRATION, { selectedCategory = SettingsCategory.INTEGRATION }, icon = Icons.Default.Share) }
                    item { SettingsRailButton("Playback", selectedCategory == SettingsCategory.PLAYBACK, { selectedCategory = SettingsCategory.PLAYBACK }, icon = Icons.Default.Settings) }
                    item { SettingsRailButton("Trakt", selectedCategory == SettingsCategory.TRAKT, { selectedCategory = SettingsCategory.TRAKT }, icon = Icons.Default.CheckCircle) }
                    item { SettingsRailButton("About", selectedCategory == SettingsCategory.ABOUT, { selectedCategory = SettingsCategory.ABOUT }, icon = Icons.Default.Info) }
                }

                // Detail Area
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    AnimatedContent(
                        targetState = selectedCategory,
                        transitionSpec = {
                            fadeIn(tween(200)) togetherWith fadeOut(tween(180))
                        }
                    ) { category ->
                        when (category) {
                            SettingsCategory.APPEARANCE -> AppearanceSettings(currentTheme, onThemeChange)
                            SettingsCategory.LAYOUT -> LayoutSettingsPanel(layoutViewModel)
                            SettingsCategory.PLAYBACK -> PlaybackSettingsPanel(playbackViewModel)
                            SettingsCategory.TRAKT -> TraktSettingsPanel(traktViewModel)
                            SettingsCategory.INTEGRATION -> IntegrationSettingsPanel(integrationViewModel)
                            SettingsCategory.ABOUT -> AboutSettingsPanel()
                            else -> PlaceholderDetail(category.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppearanceSettings(currentTheme: AppTheme, onThemeChange: (AppTheme) -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SettingsDetailHeader("Appearance", "Choose your color theme")
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(AppTheme.entries) { theme ->
                    ThemeCard(theme, theme == currentTheme) { onThemeChange(theme) }
                }
            }
        }
    }
}



@Composable
private fun PlaceholderDetail(title: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SettingsDetailHeader(title, "Configuration options for $title")
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Text("This section is under development for Windows.", color = Color.Gray)
        }
    }
}

@Composable
private fun ThemeCard(theme: AppTheme, isSelected: Boolean, onClick: () -> Unit) {
    val palette = ThemeColors.getColorPalette(theme)
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(120.dp),
        color = NuvioColors.BackgroundCard,
        shape = RoundedCornerShape(18.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, palette.secondary) else null
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier.size(48.dp).background(palette.secondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(theme.name.lowercase().replaceFirstChar { it.uppercase() }, color = Color.White)
        }
    }
}
