package com.nuvio.tv.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    isMuted: Boolean,
    onVideoEnded: () -> Unit
) {
    val mediaPlayerComponent = remember { 
        NativeDiscovery().discover()
        EmbeddedMediaPlayerComponent() 
    }

    LaunchedEffect(url) {
        mediaPlayerComponent.mediaPlayer().media().play(url)
    }

    LaunchedEffect(isMuted) {
        mediaPlayerComponent.mediaPlayer().audio().setMuted(isMuted)
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayerComponent.release()
        }
    }

    SwingPanel(
        factory = { mediaPlayerComponent },
        modifier = modifier
    )
}
