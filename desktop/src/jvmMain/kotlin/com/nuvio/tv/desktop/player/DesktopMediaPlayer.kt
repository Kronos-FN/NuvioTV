package com.nuvio.tv.desktop.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Desktop
import java.net.URI

data class DesktopPlaybackResult(
    val launchedExternally: Boolean,
    val message: String? = null
)

class DesktopMediaPlayer {
    private val mediaPlayerComponent = EmbeddedMediaPlayerComponent()
    private val isEmbeddedPlaybackAvailable: Boolean

    init {
        isEmbeddedPlaybackAvailable = NativeDiscovery().discover()
    }

    fun play(mediaPath: String): DesktopPlaybackResult {
        if (!isEmbeddedPlaybackAvailable) {
            return launchExternalPlayer(mediaPath)
        }

        mediaPlayerComponent.mediaPlayer().media().play(mediaPath)
        return DesktopPlaybackResult(launchedExternally = false)
    }

    fun stop() {
        mediaPlayerComponent.mediaPlayer().controls().stop()
    }

    @Composable
    fun VideoSurface(modifier: Modifier = Modifier) {
        if (!isEmbeddedPlaybackAvailable) {
            return
        }

        val component = remember { mediaPlayerComponent }

        DisposableEffect(Unit) {
            onDispose {
                // Ensure playback is stopped when the surface is disposed
                component.mediaPlayer().controls().stop()
            }
        }

        SwingPanel(
            factory = { component },
            modifier = modifier
        )
    }

    private fun launchExternalPlayer(mediaPath: String): DesktopPlaybackResult {
        val knownPlayers = listOf(
            "mpv.exe",
            "vlc.exe",
            "mpc-hc64.exe",
            "mpc-hc.exe",
            "PotPlayerMini64.exe",
            "PotPlayerMini.exe"
        )

        for (player in knownPlayers) {
            runCatching {
                ProcessBuilder(player, mediaPath)
                    .redirectErrorStream(true)
                    .start()
            }.onSuccess {
                return DesktopPlaybackResult(
                    launchedExternally = true,
                    message = "Opened in $player because embedded VLC playback is unavailable."
                )
            }
        }

        return runCatching {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI(mediaPath))
                DesktopPlaybackResult(
                    launchedExternally = true,
                    message = "Opened in your system default media app because embedded VLC playback is unavailable."
                )
            } else {
                DesktopPlaybackResult(
                    launchedExternally = true,
                    message = "Embedded VLC playback is unavailable and no external player was found."
                )
            }
        }.getOrElse {
            DesktopPlaybackResult(
                launchedExternally = true,
                message = "Playback failed: install VLC/MPV or configure a default media player for this stream URL."
            )
        }
    }
}
