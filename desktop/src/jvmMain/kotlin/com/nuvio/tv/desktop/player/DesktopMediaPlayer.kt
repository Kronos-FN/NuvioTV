package com.nuvio.tv.desktop.player

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component

class DesktopMediaPlayer {
    private val mediaPlayerComponent = EmbeddedMediaPlayerComponent()

    init {
        NativeDiscovery().discover()
    }

    fun play(mediaPath: String) {
        mediaPlayerComponent.mediaPlayer().media().play(mediaPath)
    }

    @Composable
    fun VideoSurface(modifier: Modifier = Modifier) {
        val component = remember { mediaPlayerComponent }
        SwingPanel(
            factory = { component },
            modifier = modifier
        )
    }
}
