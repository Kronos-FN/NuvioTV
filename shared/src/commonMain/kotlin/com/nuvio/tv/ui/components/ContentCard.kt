package com.nuvio.tv.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nuvio.tv.domain.model.MetaPreview
import com.nuvio.tv.domain.model.PosterShape
import com.nuvio.tv.ui.theme.NuvioColors
import com.nuvio.tv.ui.theme.NuvioTheme
import kotlinx.coroutines.delay

private const val BACKDROP_ASPECT_RATIO = 16f / 9f
private val YEAR_REGEX = Regex("""\b(19|20)\d{2}\b""")

@Composable
fun ContentCard(
    item: MetaPreview,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    posterCardStyle: PosterCardStyle = PosterCardDefaults.Style,
    showLabels: Boolean = true,
    focusedPosterBackdropExpandEnabled: Boolean = false,
    focusedPosterBackdropExpandDelaySeconds: Int = 3,
    focusedPosterBackdropTrailerEnabled: Boolean = false,
    focusedPosterBackdropTrailerMuted: Boolean = true,
    trailerPreviewUrl: String? = null,
    onRequestTrailerPreview: (MetaPreview) -> Unit = {},
    onFocus: (MetaPreview) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val cardShape = RoundedCornerShape(posterCardStyle.cornerRadius)
    val baseCardWidth = when (item.posterShape) {
        PosterShape.POSTER -> posterCardStyle.width
        PosterShape.LANDSCAPE -> 260.dp
        PosterShape.SQUARE -> 170.dp
    }
    val baseCardHeight = when (item.posterShape) {
        PosterShape.POSTER -> posterCardStyle.height
        PosterShape.LANDSCAPE -> 148.dp
        PosterShape.SQUARE -> 170.dp
    }
    val expandedCardWidth = baseCardHeight * BACKDROP_ASPECT_RATIO

    var isFocused by remember(item.id) { mutableStateOf(false) }
    var interactionNonce by remember(item.id) { mutableIntStateOf(0) }
    var isBackdropExpanded by remember(item.id) { mutableStateOf(false) }
    var trailerFirstFrameRendered by remember(item.id, trailerPreviewUrl) { mutableStateOf(false) }

    LaunchedEffect(focusedPosterBackdropExpandEnabled, focusedPosterBackdropExpandDelaySeconds, isFocused, interactionNonce, item.id) {
        if (!focusedPosterBackdropExpandEnabled || !isFocused) {
            isBackdropExpanded = false
            return@LaunchedEffect
        }
        delay(focusedPosterBackdropExpandDelaySeconds.coerceAtLeast(1) * 1000L)
        if (isFocused) isBackdropExpanded = true
    }

    LaunchedEffect(item.id, isFocused, focusedPosterBackdropTrailerEnabled, trailerPreviewUrl) {
        if (focusedPosterBackdropTrailerEnabled && isFocused && trailerPreviewUrl == null) {
            onRequestTrailerPreview(item)
        }
    }

    val animatedCardWidth by animateDpAsState(if (isBackdropExpanded) expandedCardWidth else baseCardWidth)
    val cardScale by animateFloatAsState(if (isFocused) posterCardStyle.focusedScale else 1f)
    
    val metaTokens = remember(item) {
        buildList {
            add(item.apiType.replaceFirstChar { it.uppercase() })
            item.genres.firstOrNull()?.let { add(it) }
            item.releaseInfo?.let { YEAR_REGEX.find(it)?.value }?.let { add(it) }
            item.imdbRating?.let { add("%.1f".format(it)) }
        }
    }

    Column(modifier = modifier.width(animatedCardWidth).graphicsLayer {
        scaleX = cardScale
        scaleY = cardScale
    }) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(baseCardHeight)
                .onFocusChanged { 
                    isFocused = it.isFocused
                    if (it.isFocused) {
                        interactionNonce++
                        onFocus(item)
                    }
                }
                .onPreviewKeyEvent { 
                    if (isFocused && it.type == KeyEventType.KeyDown) {
                        interactionNonce++
                    }
                    false
                }
                .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
            shape = cardShape,
            color = NuvioColors.BackgroundCard,
            border = if (isFocused) BorderStroke(posterCardStyle.focusedBorderWidth, NuvioColors.FocusRing) else null
        ) {
            Box(modifier = Modifier.fillMaxSize().clip(cardShape)) {
                AsyncImage(
                    model = if (isBackdropExpanded) item.background ?: item.poster else item.poster,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (isBackdropExpanded && focusedPosterBackdropTrailerEnabled && isFocused && trailerPreviewUrl != null) {
                    TrailerPlayer(
                        trailerUrl = trailerPreviewUrl,
                        isPlaying = true,
                        onEnded = { isBackdropExpanded = false },
                        onFirstFrameRendered = { trailerFirstFrameRendered = true },
                        muted = focusedPosterBackdropTrailerMuted,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (isBackdropExpanded) {
                    Box(
                        modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().height(96.dp)
                            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.8f))))
                    )
                    
                    if (item.logo != null) {
                        AsyncImage(
                            model = item.logo,
                            contentDescription = item.name,
                            modifier = Modifier.align(Alignment.BottomStart).padding(12.dp).height(40.dp).fillMaxWidth(0.7f),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            text = item.name,
                            modifier = Modifier.align(Alignment.BottomStart).padding(12.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        if (showLabels || isBackdropExpanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                if (!isBackdropExpanded) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium, color = NuvioColors.TextPrimary, maxLines = 1)
                    item.releaseInfo?.let { Text(it, style = MaterialTheme.typography.labelMedium, color = NuvioColors.TextSecondary) }
                } else {
                    Text(metaTokens.joinToString("  •  "), style = MaterialTheme.typography.labelMedium, color = NuvioColors.TextSecondary)
                    item.description?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = NuvioColors.TextPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                }
            }
        }
    }
}
