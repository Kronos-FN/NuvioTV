package com.nuvio.tv.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent

@Composable
actual fun TrailerPlayer(
    trailerUrl: String?,
    isPlaying: Boolean,
    onEnded: () -> Unit,
    onFirstFrameRendered: () -> Unit,
    muted: Boolean,
    modifier: Modifier
) {
    val mediaPlayerComponent = remember { 
        NativeDiscovery().discover()
        EmbeddedMediaPlayerComponent() 
    }
    
    val currentOnEnded by rememberUpdatedState(onEnded)
    val currentOnFirstFrameRendered by rememberUpdatedState(onFirstFrameRendered)

    LaunchedEffect(trailerUrl, isPlaying, muted) {
        val player = mediaPlayerComponent.mediaPlayer()
        player.audio().setMute(muted)
        
        if (isPlaying && trailerUrl != null) {
            player.media().play(trailerUrl)
            // Simulating first frame rendered as vlcj doesn't have a direct callback for this in Compose
            currentOnFirstFrameRendered()
        } else {
            player.controls().stop()
        }
    }
    
    DisposableEffect(Unit) {
        val player = mediaPlayerComponent.mediaPlayer()
        onDispose {
            player.controls().stop()
            mediaPlayerComponent.release()
        }
    }

    SwingPanel(
        factory = { mediaPlayerComponent },
        modifier = modifier
    )
}
