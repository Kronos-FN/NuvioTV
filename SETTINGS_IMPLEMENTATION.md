# Settings UI Implementation - Desktop Port

## Overview

This document describes the implementation of the full settings UI for the NuvioTV Desktop application. The settings have been ported from the Android TV app to the shared Kotlin Multiplatform module, ensuring they work on desktop while maintaining the same functionality.

## Architecture

### Domain Layer (`shared/src/commonMain/kotlin/com/nuvio/tv/domain/model/`)

**New Domain Models:**
- `PlayerPreference.kt` - Enums and data classes for player settings
  - `PlayerPreference` enum (INTERNAL, EXTERNAL)
  - `DecoderPriority` enum (AUTO, HARDWARE, SOFTWARE)
  - `SubtitleStyle` data class
- `LayoutPreferences.kt` - Layout configuration data class
- `PlayerPreferences.kt` - Playback configuration data class
- `TraktPreferences.kt` - Trakt authentication and sync data
  - `TraktConnectionMode` enum (DISCONNECTED, AWAITING_APPROVAL, CONNECTED)
  - `TraktDeviceCode` data class
- `IntegrationPreferences.kt` - External service integration settings

### Data Layer (`shared/src/commonMain/kotlin/com/nuvio/tv/data/preferences/`)

**Preference Stores (Simple StateFlow-based):**
- `LayoutPreferencesStore.kt` - Manages layout settings in-memory
- `PlayerPreferencesStore.kt` - Manages playback settings in-memory
- `TraktPreferencesStore.kt` - Manages Trakt connection state
- `IntegrationPreferencesStore.kt` - Manages integration settings (TMDB, MDBList, Real-Debrid)

All stores use `MutableStateFlow` for state management, avoiding Android-specific DataStore dependencies.

### Presentation Layer

**ViewModels (`shared/src/commonMain/kotlin/com/nuvio/tv/ui/viewmodel/`):**
- `LayoutSettingsViewModel.kt` - Layout settings business logic
- `PlaybackSettingsViewModel.kt` - Playback settings business logic  
- `TraktViewModel.kt` - Trakt connection flow management
- `IntegrationSettingsViewModel.kt` - Integration settings management

**UI Components (`shared/src/commonMain/kotlin/com/nuvio/tv/ui/screens/settings/`):**

**Design System (`SettingsDesignSystem.kt`):**
- `SettingsWorkspaceSurface` - Main container with rounded corners
- `SettingsRailButton` - Sidebar category buttons
- `SettingsDetailHeader` - Panel header with title and subtitle
- `SettingsGroupCard` - Content grouping card
- `SettingsActionRow` - Clickable row with arrow
- `SettingsToggleRow` - Row with a switch toggle
- `SettingsSliderRow` - Row with a slider for numeric values
- `SettingsChoiceChip` - Selectable chip for options
- `SettingsButton` - Primary action button

**Settings Panels:**
- `LayoutSettingsPanel.kt` - Home layout, content visibility, poster styling
- `PlaybackSettingsPanel.kt` - Player, audio, subtitles, behavior, skip controls
- `TraktSettingsPanel.kt` - Trakt connection with three states (disconnected, awaiting, connected)
- `IntegrationSettingsPanel.kt` - Debrid services, TMDB enrichment, MDBList
- `AboutSettingsPanel.kt` - Version info, links, credits, license

**Main Screen (`SettingsScreen.kt`):**
- Wires all panels together with animated transitions
- Creates store and ViewModel instances
- Manages sidebar navigation and panel routing

## Features Implemented

### 1. Layout Settings
- [x] Home layout selector (Classic vs Grid)
- [x] Home content toggles (hero section, poster labels, addon names, continue watching)
- [x] Detail page options (trailer button, external metadata preference)
- [x] Focused poster delay slider (1-10 seconds)
- [x] Poster card size presets (Small, Medium, Large)
- [x] Poster card corner radius presets (Round, Rounded, Sharp)
- [x] Reset to default button

### 2. Playback Settings
- [x] Player preference selector (Internal vs External)
- [x] Decoder priority selector (Auto, Hardware, Software)
- [x] Audio language picker (placeholder)
- [x] Subtitle language picker (placeholder)
- [x] Subtitle size slider (12-32pt)
- [x] Auto-play next episode toggle
- [x] Resume playback toggle
- [x] Skip intro toggle
- [x] Skip outro toggle
- [x] Auto-play trailers toggle

### 3. Trakt Settings
- [x] Disconnected state with features list and "Connect" button
- [x] Awaiting approval state showing device code and verification URL
- [x] Connected state with username, token expiry, and disconnect button
- [x] Continue watching days cap slider (7-90 days)
- [x] Note: API integration is placeholder - UI is complete and ready for real API calls

### 4. Integration Settings
- [x] Real-Debrid connection row (placeholder)
- [x] AllDebrid row (coming soon)
- [x] TMDB enrichment enable toggle
- [x] TMDB language selector (placeholder)
- [x] TMDB metadata toggles (descriptions, ratings, posters, backdrops, logos)
- [x] MDBList enable toggle
- [x] MDBList API key configuration (placeholder dialog)

### 5. About Settings
- [x] App name and version display
- [x] "Check for Updates" button (placeholder)
- [x] Links section (privacy policy, terms, GitHub, report issue)
- [x] Credits section
- [x] License information

## Technical Notes

### Multiplatform Compatibility
- All code uses standard Kotlin Multiplatform libraries
- No Android-specific imports (`android.*`, `androidx.tv.*`, `androidx.hilt.*`)
- Uses `androidx.compose.material3.*` instead of `androidx.tv.material3.*`
- No dependency injection framework required (stores created directly)

### State Management
- Uses `kotlinx.coroutines.flow.StateFlow` for reactive state
- ViewModels are simple classes (not Android's `ViewModel`)
- All state is currently in-memory (can be extended with persistence layer)

### UI Design
- Follows existing NuvioTheme design language
- Dark background with elevated cards
- Accent colors from NuvioColors
- Consistent spacing and typography
- Smooth animated transitions between panels

## Future Enhancements

### Persistence
The current implementation uses in-memory state. To add persistence:
1. Create `expect/actual` declarations for file I/O
2. Implement platform-specific DataStore or file-based storage
3. Load initial state in store constructors
4. Auto-save on state changes

### Dialog Implementations
The following dialogs are placeholder and need implementation:
- Language picker dialog (for audio/subtitle languages)
- TMDB language selector
- MDBList API key input dialog
- Real-Debrid connection dialog

### API Integration
Placeholder TODOs for:
- Trakt device code flow API calls
- Trakt token refresh
- Real-Debrid authentication
- Update check functionality
- External link opening

## Testing

The implementation has been code-reviewed but cannot be compiled in the current environment due to Java toolchain requirements (requires Java 21, environment has Java 17).

**Manual Testing Checklist:**
1. Navigate to Settings screen
2. Click each category in the sidebar
3. Verify all panels load without errors
4. Test all toggles, sliders, and buttons
5. Verify state changes persist within session
6. Test theme switching from Appearance panel
7. Verify Trakt connection flow UI states
8. Confirm all choice chips are selectable
9. Test reset to default on poster card style
10. Verify smooth animations between panels

## Implementation Status

✅ **Complete:**
- Domain models for all settings
- Preference stores for state management
- ViewModels for business logic
- All UI panels implemented
- Design system components
- Main settings screen wiring

⚠️ **Placeholder/Future Work:**
- Persistence layer (currently in-memory only)
- Language picker dialogs
- API integration for Trakt
- Real-Debrid authentication
- MDBList API key dialog
- External link opening
- Update checker

## Migration from Upstream

The implementation draws from these upstream Android TV app files:
- `LayoutSettingsViewModel.kt` → Adapted to shared LayoutSettingsViewModel
- `PlaybackSettingsViewModel.kt` → Adapted to shared PlaybackSettingsViewModel
- `TraktViewModel.kt` → Adapted to shared TraktViewModel
- `LayoutSettingsScreen.kt` → Adapted to LayoutSettingsPanel
- `PlaybackSettingsScreen.kt` → Adapted to PlaybackSettingsPanel
- `TraktScreen.kt` → Adapted to TraktSettingsPanel
- `TmdbSettingsScreen.kt` → Integrated into IntegrationSettingsPanel
- `MDBListSettingsScreen.kt` → Integrated into IntegrationSettingsPanel
- `AboutScreen.kt` → Adapted to AboutSettingsPanel

All Android-specific dependencies (Hilt, Android ViewModel, TV Material3, DataStore) have been removed or replaced with multiplatform alternatives.
