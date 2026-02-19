package com.nuvio.tv.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.nuvio.tv.desktop.di.initKoin
import com.nuvio.tv.desktop.player.DesktopMediaPlayer
import com.nuvio.tv.domain.model.AppTheme
import com.nuvio.tv.domain.repository.MetaRepository
import com.nuvio.tv.domain.repository.StreamRepository
import com.nuvio.tv.ui.components.NavigationItem
import com.nuvio.tv.ui.components.SidebarNavigation
import com.nuvio.tv.ui.navigation.*
import com.nuvio.tv.ui.screens.addon.AddonManagerScreen
import com.nuvio.tv.ui.screens.detail.MetaDetailsScreen
import com.nuvio.tv.ui.screens.detail.MetaDetailsViewModel
import com.nuvio.tv.ui.screens.home.HomeScreen
import com.nuvio.tv.ui.screens.home.HomeViewModel
import com.nuvio.tv.ui.screens.player.PlayerScreen
import com.nuvio.tv.ui.screens.search.SearchScreen
import com.nuvio.tv.ui.screens.search.SearchViewModel
import com.nuvio.tv.ui.screens.settings.SettingsScreen
import com.nuvio.tv.ui.screens.stream.StreamScreen
import com.nuvio.tv.ui.screens.stream.StreamScreenViewModel
import com.nuvio.tv.ui.theme.NuvioTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.compose.koinInject

fun main() {
    initKoin()

    application {
        val windowState = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            width = 1400.dp,
            height = 900.dp
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "Nuvio Media Hub",
            state = windowState
        ) {
            var currentTheme by remember { mutableStateOf(AppTheme.OCEAN) }
            
            NuvioTheme(appTheme = currentTheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainContent(
                        currentTheme = currentTheme,
                        onThemeChange = { currentTheme = it }
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit
) {
    val homeViewModel: HomeViewModel = koinInject()
    val searchViewModel: SearchViewModel = koinInject()
    val metaRepository: MetaRepository = koinInject()
    val streamRepository: StreamRepository = koinInject()
    
    val navigationController = remember { DesktopNavigationController() }
    val mediaPlayer = remember { DesktopMediaPlayer() }
    
    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar navigation
        val currentScreen = remember { 
            derivedStateOf { 
                when (navigationController.currentScreen) {
                    is DesktopScreen.Home -> NavigationItem.Home
                    is DesktopScreen.Search -> NavigationItem.Search
                    is DesktopScreen.Library -> NavigationItem.Library
                    is DesktopScreen.Addons -> NavigationItem.Addons
                    is DesktopScreen.Settings -> NavigationItem.Settings
                    else -> NavigationItem.Home
                }
            }
        }
        
        SidebarNavigation(
            selectedItem = currentScreen.value,
            onItemSelected = { item ->
                when (item) {
                    NavigationItem.Home -> navigationController.navigateToRoot()
                    NavigationItem.Search -> navigationController.navigate(DesktopScreen.Search)
                    NavigationItem.Library -> navigationController.navigate(DesktopScreen.Library)
                    NavigationItem.Addons -> navigationController.navigate(DesktopScreen.Addons)
                    NavigationItem.Settings -> navigationController.navigate(DesktopScreen.Settings)
                }
            }
        )

        // Main content area
        DesktopNavHost(navigationController) { screen, navigate, navigateBack ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (screen) {
                    is DesktopScreen.Home -> {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToDetail = { id, type, baseUrl ->
                                navigate(DesktopScreen.Detail(id, type, baseUrl))
                            }
                        )
                    }
                    is DesktopScreen.Search -> {
                        SearchScreen(
                            viewModel = searchViewModel,
                            onNavigateToDetail = { id, type, baseUrl ->
                                navigate(DesktopScreen.Detail(id, type, baseUrl))
                            }
                        )
                    }
                    is DesktopScreen.Library -> {
                        // Placeholder for library screen
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Library (Coming Soon)", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                    is DesktopScreen.Addons -> {
                        AddonManagerScreen(
                            viewModel = homeViewModel
                        )
                    }
                    is DesktopScreen.Settings -> {
                        SettingsScreen(
                            currentTheme = currentTheme,
                            onThemeChange = onThemeChange
                        )
                    }
                    is DesktopScreen.Detail -> {
                        // Create ViewModel if needed
                        val viewModel = remember(screen.id, screen.type) {
                            MetaDetailsViewModel(
                                metaRepository = metaRepository,
                                coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
                                itemId = screen.id,
                                itemType = screen.type,
                                preferredAddonBaseUrl = screen.baseUrl
                            ).also {
                                it.onNavigateToStream = { videoId, contentType, title, season, episode ->
                                    navigate(DesktopScreen.Stream(videoId, contentType, title, season, episode))
                                }
                                it.onNavigateBack = { navigateBack() }
                            }
                        }
                        
                        MetaDetailsScreen(viewModel = viewModel)
                    }
                    is DesktopScreen.Stream -> {
                        // Create ViewModel if needed
                        val viewModel = remember(screen.videoId, screen.contentType) {
                            StreamScreenViewModel(
                                streamRepository = streamRepository,
                                coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
                                videoId = screen.videoId,
                                contentType = screen.contentType,
                                title = screen.title,
                                season = screen.season,
                                episode = screen.episode
                            ).also {
                                it.onNavigateToPlayer = { streamUrl, title, headers ->
                                    mediaPlayer.play(streamUrl)
                                    navigate(DesktopScreen.Player(streamUrl, title, headers))
                                }
                                it.onNavigateBack = { navigateBack() }
                            }
                        }
                        
                        StreamScreen(viewModel = viewModel)
                    }
                    is DesktopScreen.Player -> {
                        PlayerScreen(
                            title = screen.title,
                            onBackClick = { navigateBack() },
                            videoSurface = {
                                mediaPlayer.VideoSurface(modifier = Modifier.fillMaxSize())
                            }
                        )
                    }
                }
            }
        }
    }
}
