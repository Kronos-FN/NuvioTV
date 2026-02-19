# NuvioTV Windows Migration Guide (Compose Desktop)

This project is already structured as a Kotlin Multiplatform app with an Android target (`app`) and a Desktop/Windows target (`desktop` + `shared`).

## What is already implemented

- Shared UI/business logic in `shared/src/commonMain`.
- Windows packaging via Compose Desktop in `desktop/build.gradle.kts` (`TargetFormat.Exe`).
- Desktop playback implementation in `shared/src/desktopMain` and `desktop/src/jvmMain`.

## Windows player strategy

NuvioTV supports two playback modes on Windows:

1. **Embedded playback (preferred):** VLC native libraries discovered via `vlcj`.
2. **External playback fallback:** If embedded playback is unavailable, NuvioTV attempts known Windows players (`mpv`, `VLC`, `MPC-HC`, `PotPlayer`) and then the system default app.

This gives Windows users a reliable playback path even when VLC native discovery fails.

## Build and run on Windows

From repository root:

```bash
./gradlew :desktop:run
```

Build installer:

```bash
./gradlew :desktop:packageExe :desktop:copyExeToRoot
```

Installer output is copied to:

- `NuvioTV_Installer.exe` in the repository root.

## Feature parity checklist

To match Android behavior as closely as possible on Windows, validate:

- Home/search/detail/stream navigation.
- Addon catalog loading and metadata parsing.
- Stream playback (embedded and fallback external mode).
- Settings panels (theme, integrations, playback/layout/trakt).
- Trakt and integration flows.
- Library and continue-watching behavior.

## Notes for contributors

- Prefer adding logic in `shared/commonMain` unless platform APIs require `desktopMain`/`androidMain`.
- Keep desktop-specific player behavior isolated to desktop modules.
- If adding a new external player, update detection/launch order in desktop player code.
