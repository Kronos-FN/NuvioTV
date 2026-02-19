package com.nuvio.tv.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TrailerPlayer(
    trailerUrl: String?,
    isPlaying: Boolean,
    onEnded: () -> Unit,
    onFirstFrameRendered: () -> Unit,
    muted: Boolean,
    modifier: Modifier
) {
    if (trailerUrl != null && isPlaying) {
        VideoPlayer(
            url = trailerUrl,
            modifier = modifier,
            isMuted = muted,
            onVideoEnded = onEnded
        )
    }
}
