package com.nuvio.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

class NuvioColorScheme(palette: ThemeColorPalette) {
    val Background = palette.background
    val BackgroundElevated = palette.backgroundElevated
    val BackgroundCard = palette.backgroundCard

    val Surface = Color(0xFF1E1E1E)
    val SurfaceVariant = Color(0xFF2D2D2D)

    val Primary = Color(0xFF9E9E9E)
    val PrimaryVariant = Color(0xFF6F6F6F)
    val OnPrimary = Color(0xFFFFFFFF)

    val Secondary = palette.secondary
    val SecondaryVariant = palette.secondaryVariant

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB3B3B3)
    val TextTertiary = Color(0xFF808080)
    val TextDisabled = Color(0xFF4D4D4D)

    val FocusRing = palette.focusRing
    val FocusBackground = palette.focusBackground

    val Rating = Color(0xFFFFD700)
    val Error = Color(0xFFCF6679)
    val Success = Color(0xFF4CAF50)

    val Border = Color(0xFF333333)
    val BorderFocused = palette.focusRing
}

object NuvioColors {
    val Background: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.Background

    val BackgroundElevated: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.BackgroundElevated

    val BackgroundCard: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.BackgroundCard

    val Surface = Color(0xFF1E1E1E)
    val SurfaceVariant = Color(0xFF2D2D2D)

    val Primary = Color(0xFF9E9E9E)
    val PrimaryVariant = Color(0xFF6F6F6F)
    val OnPrimary = Color(0xFFFFFFFF)

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB3B3B3)
    val TextTertiary = Color(0xFF808080)
    val TextDisabled = Color(0xFF4D4D4D)

    val Rating = Color(0xFFFFD700)
    val Error = Color(0xFFCF6679)
    val Success = Color(0xFF4CAF50)

    val Border = Color(0xFF333333)

    val Secondary: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.Secondary

    val SecondaryVariant: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.SecondaryVariant

    val FocusRing: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.FocusRing

    val FocusBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.FocusBackground

    val BorderFocused: Color
        @Composable
        @ReadOnlyComposable
        get() = NuvioTheme.colors.BorderFocused
}
