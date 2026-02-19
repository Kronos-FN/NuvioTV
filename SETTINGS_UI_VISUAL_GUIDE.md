# Settings UI Visual Guide

This document provides a visual description of the implemented settings UI.

## Main Settings Screen Layout

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  Settings (32dp padding)                                                    │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │ Settings Workspace Surface (28dp rounded, elevated background)        │ │
│  │ ┌──────────────────┬───────────────────────────────────────────────┐ │ │
│  │ │ Sidebar (282dp)  │ Detail Panel (flexible width)                 │ │ │
│  │ │                  │                                                │ │ │
│  │ │ ○ Account        │  [Current Selected Category Content]          │ │ │
│  │ │ ● Appearance     │                                                │ │ │
│  │ │ ○ Layout         │  Animated transitions (fade 200ms)            │ │ │
│  │ │ ○ Plugins        │                                                │ │ │
│  │ │ ○ Integration    │  Scrollable content with:                     │ │ │
│  │ │ ○ Playback       │  - Headers                                    │ │ │
│  │ │ ○ Trakt          │  - Group cards                                │ │ │
│  │ │ ○ About          │  - Settings rows                              │ │ │
│  │ │                  │  - Toggles, sliders, chips                    │ │ │
│  │ └──────────────────┴───────────────────────────────────────────────┘ │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Layout Settings Panel

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Layout                                                                  │
│ Customize your home screen layout and appearance                       │
├─────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ Home Layout                                                         │ │
│ │ ┌─────────────────┐ ┌─────────────────┐                           │ │
│ │ │    Classic     │ │      Grid       │                           │ │
│ │ └─────────────────┘ └─────────────────┘                           │ │
│ │                                                                     │ │
│ │ Home Content                                                        │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Hero Section                              [Toggle: ON  ]    │   │ │
│ │ │ Show featured content at the top of home                    │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Poster Labels                             [Toggle: ON  ]    │   │ │
│ │ │ Display titles below content posters                        │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │ ... (more toggles)                                                 │ │
│ │                                                                     │ │
│ │ Poster Card Style                                                  │ │
│ │ ┌───────┐ ┌────────┐ ┌───────┐                                    │ │
│ │ │ Small │ │ Medium │ │ Large │                                    │ │
│ │ └───────┘ └────────┘ └───────┘                                    │ │
│ │                                                                     │ │
│ │ ┌───────────────────────────────────────────────────────────┐     │ │
│ │ │              Reset to Default                             │     │ │
│ │ └───────────────────────────────────────────────────────────┘     │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

## Playback Settings Panel

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Playback                                                                │
│ Configure player, audio, and subtitle preferences                      │
├─────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ Player                                                              │ │
│ │ ┌──────────────┐ ┌──────────────┐                                 │ │
│ │ │   Internal   │ │   External   │                                 │ │
│ │ └──────────────┘ └──────────────┘                                 │ │
│ │                                                                     │ │
│ │ Decoder Priority                                                   │ │
│ │ ┌──────┐ ┌──────────┐ ┌──────────┐                               │ │
│ │ │ Auto │ │ Hardware │ │ Software │                               │ │
│ │ └──────┘ └──────────┘ └──────────┘                               │ │
│ │                                                                     │ │
│ │ Subtitles                                                          │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Subtitle Size                                 16pt          │   │ │
│ │ │ Adjust subtitle text size                                   │   │ │
│ │ │ ├───────●────────────────────────────────────┤              │   │ │
│ │ │ 12pt                                      32pt              │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │                                                                     │ │
│ │ Behavior                                                           │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Auto-play Next Episode                    [Toggle: ON  ]    │   │ │
│ │ │ Automatically play next episode when current ends           │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

## Trakt Settings Panel (Connected State)

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Trakt                                                                   │
│ Connect your Trakt account for syncing and tracking                    │
├─────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                                                                     │ │
│ │ Connected                                                           │ │
│ │ Logged in as: username123                                          │ │
│ │ Token expires in ~89 days                                          │ │
│ │                                                                     │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Continue Watching Days Cap              30 days             │   │ │
│ │ │ How many days of history to show                            │   │ │
│ │ │ ├────────────●──────────────────────────────────┤           │   │ │
│ │ │ 7                                              90            │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │                                                                     │ │
│ │ ┌───────────────────────────────────────────────────────────┐     │ │
│ │ │                    Disconnect                             │     │ │
│ │ └───────────────────────────────────────────────────────────┘     │ │
│ │                                                                     │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

## Integration Settings Panel

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Integration                                                             │
│ Configure external services and metadata sources                       │
├─────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ Debrid Services                                                     │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Real-Debrid                        Not connected        ➤   │   │ │
│ │ │ Connect your account for high-speed streaming               │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │                                                                     │ │
│ │ TMDB Enrichment                                                    │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Enable TMDB                               [Toggle: OFF ]    │   │ │
│ │ │ Use The Movie Database for metadata enrichment              │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │                                                                     │ │
│ │ (When enabled, shows language and metadata options)               │ │
│ │ - Descriptions, Ratings, Posters, Backdrops, Logos                │ │
│ │                                                                     │ │
│ │ MDBList Ratings                                                    │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Enable MDBList                            [Toggle: OFF ]    │   │ │
│ │ │ Show aggregated ratings from multiple sources               │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

## About Settings Panel

```
┌─────────────────────────────────────────────────────────────────────────┐
│ About                                                                   │
│ Version and policies                                                   │
├─────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                                                                     │ │
│ │                      Nuvio Media Hub                                │ │
│ │                    Version 0.3.6 (Desktop)                          │ │
│ │         A Kotlin Multiplatform media hub for all your needs.       │ │
│ │                                                                     │ │
│ │ ┌───────────────────────────────────────────────────────────┐     │ │
│ │ │               Check for Updates                           │     │ │
│ │ └───────────────────────────────────────────────────────────┘     │ │
│ │                                                                     │ │
│ │ Links                                                              │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ Privacy Policy                                         ➤    │   │ │
│ │ │ View our privacy policy                                     │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │ ┌─────────────────────────────────────────────────────────────┐   │ │
│ │ │ GitHub Repository                                      ➤    │   │ │
│ │ │ Visit the source code                                       │   │ │
│ │ └─────────────────────────────────────────────────────────────┘   │ │
│ │                                                                     │ │
│ │ Credits                                                            │ │
│ │ Developed by the Nuvio team                                       │ │
│ │ Desktop port by JaidenGoode                                       │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

## UI Components

### SettingsToggleRow
```
┌─────────────────────────────────────────────────────────────────┐
│ Setting Name                                      ○─────┐       │
│ Optional subtitle description                            │       │
└─────────────────────────────────────────────────────────────────┘
```

### SettingsSliderRow
```
┌─────────────────────────────────────────────────────────────────┐
│ Setting Name                                          Value     │
│ Optional subtitle description                                   │
│ ├───────────●──────────────────────────────────────┤           │
│ Min                                               Max           │
└─────────────────────────────────────────────────────────────────┘
```

### SettingsChoiceChip (Multiple)
```
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│   Option 1   │ │   Option 2   │ │   Option 3   │
└──────────────┘ └──────────────┘ └──────────────┘
     selected        normal          normal
   (highlighted)   (secondary)     (secondary)
```

### SettingsActionRow
```
┌─────────────────────────────────────────────────────────────────┐
│ Setting Name                                              ➤     │
│ Optional subtitle or current value                              │
└─────────────────────────────────────────────────────────────────┘
```

## Color Scheme
- Background: Dark (NuvioColors.Background)
- Elevated surfaces: Slightly lighter dark (NuvioColors.BackgroundElevated)
- Cards: Dark with subtle elevation (NuvioColors.BackgroundCard)
- Primary text: White (NuvioColors.TextPrimary)
- Secondary text: Light gray (NuvioColors.TextSecondary)
- Accent: Theme-dependent (NuvioColors.Accent)
- Borders: Subtle dark (NuvioColors.Border)
- Focus ring: Accent color (NuvioColors.FocusRing)

## Typography
- Headers: MaterialTheme.typography.headlineMedium
- Titles: MaterialTheme.typography.titleMedium/titleLarge
- Body: MaterialTheme.typography.bodyLarge/bodyMedium
- Small: MaterialTheme.typography.bodySmall

## Spacing
- Screen padding: 32dp
- Card border radius: 28dp (main), 18dp (secondary)
- Pill radius: 999dp (fully rounded)
- Row height: 62dp minimum
- Section spacing: 14dp
- Item spacing: 10dp
- Horizontal padding: 18dp
