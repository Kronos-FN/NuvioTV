package com.nuvio.tv.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.nuvio.tv.desktop.di.initKoin
import com.nuvio.tv.domain.model.AppTheme
import com.nuvio.tv.ui.components.NavigationItem
import com.nuvio.tv.ui.components.SidebarNavigation
import com.nuvio.tv.ui.screens.addon.AddonManagerScreen
import com.nuvio.tv.ui.screens.home.HomeScreen
import com.nuvio.tv.ui.screens.home.HomeViewModel
import com.nuvio.tv.ui.screens.search.SearchScreen
import com.nuvio.tv.ui.screens.search.SearchViewModel
import com.nuvio.tv.ui.screens.settings.SettingsScreen
import com.nuvio.tv.ui.theme.NuvioTheme
import org.koin.compose.koinInject

fun main() {
    initKoin()

    application {
        val windowState = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center)
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
    var selectedItem by remember { mutableStateOf(NavigationItem.Home) }

    Row(modifier = Modifier.fillMaxSize()) {
        SidebarNavigation(
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedItem) {
                NavigationItem.Home -> {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onNavigateToDetail = { id, type, baseUrl ->
                            println("Navigating to $id ($type) @ $baseUrl")
                        }
                    )
                }
                NavigationItem.Search -> {
                    SearchScreen(
                        viewModel = searchViewModel,
                        onNavigateToDetail = { id, type, baseUrl ->
                            println("Navigating to $id ($type) @ $baseUrl")
                        }
                    )
                }
                NavigationItem.Addons -> {
                    AddonManagerScreen(
                        viewModel = homeViewModel
                    )
                }
                NavigationItem.Settings -> {
                    SettingsScreen(
                        currentTheme = currentTheme,
                        onThemeChange = onThemeChange
                    )
                }
            }
        }
    }
}
