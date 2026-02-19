package com.nuvio.tv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simple navigation controller for the desktop app
 */
class DesktopNavigationController {
    private val _navigationStack = MutableStateFlow<List<DesktopScreen>>(listOf(DesktopScreen.Home))
    val navigationStack: StateFlow<List<DesktopScreen>> = _navigationStack.asStateFlow()
    
    val currentScreen: DesktopScreen
        get() = _navigationStack.value.lastOrNull() ?: DesktopScreen.Home
    
    fun navigate(screen: DesktopScreen) {
        val current = _navigationStack.value.toMutableList()
        current.add(screen)
        _navigationStack.value = current
    }
    
    fun navigateBack() {
        val current = _navigationStack.value
        if (current.size > 1) {
            _navigationStack.value = current.dropLast(1)
        }
    }
    
    fun navigateToRoot() {
        _navigationStack.value = listOf(DesktopScreen.Home)
    }
    
    fun replaceCurrent(screen: DesktopScreen) {
        val current = _navigationStack.value.toMutableList()
        if (current.isNotEmpty()) {
            current[current.lastIndex] = screen
        } else {
            current.add(screen)
        }
        _navigationStack.value = current
    }
    
    fun canNavigateBack(): Boolean = _navigationStack.value.size > 1
}

@Composable
fun DesktopNavHost(
    navigationController: DesktopNavigationController,
    content: @Composable (DesktopScreen, (DesktopScreen) -> Unit, () -> Unit) -> Unit
) {
    val stack by navigationController.navigationStack.collectAsState()
    val currentScreen = stack.lastOrNull() ?: DesktopScreen.Home
    
    content(
        currentScreen,
        { screen -> navigationController.navigate(screen) },
        { navigationController.navigateBack() }
    )
}
