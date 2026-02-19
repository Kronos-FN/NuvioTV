package com.nuvio.tv.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.base.State

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    isMuted: Boolean,
    onVideoEnded: () -> Unit
) {
    // Initialize VLC native discovery once
    remember { NativeDiscovery().discover() }

    val mediaPlayerComponent = remember { EmbeddedMediaPlayerComponent() }
    val currentOnVideoEnded by rememberUpdatedState(onVideoEnded)

    LaunchedEffect(url, isMuted) {
        val mediaPlayer = mediaPlayerComponent.mediaPlayer()
        mediaPlayer.audio().setMute(isMuted)
        
        if (url.isNotBlank()) {
            mediaPlayer.media().play(url)
        }
    }

    DisposableEffect(Unit) {
        val mediaPlayer = mediaPlayerComponent.mediaPlayer()
        
        val listener = object : uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {
            override fun finished(mediaPlayer: uk.co.caprica.vlcj.player.base.MediaPlayer?) {
                currentOnVideoEnded()
            }
            override fun error(mediaPlayer: uk.co.caprica.vlcj.player.base.MediaPlayer?) {
                // Handle error if needed
            }
        }
        
        mediaPlayer.events().addMediaPlayerEventListener(listener)
        
        onDispose {
            mediaPlayer.events().removeMediaPlayerEventListener(listener)
            mediaPlayer.controls().stop()
            mediaPlayerComponent.release()
        }
    }

    SwingPanel(
        factory = { mediaPlayerComponent },
        modifier = modifier
    )
}
