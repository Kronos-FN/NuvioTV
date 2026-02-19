package com.nuvio.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nuvio.tv.ui.screens.home.ContinueWatchingItem
import com.nuvio.tv.ui.theme.NuvioColors

private val CwCardShape = RoundedCornerShape(12.dp)
private val CwClipShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
private val BadgeShape = RoundedCornerShape(4.dp)

@Composable
fun ContinueWatchingSection(
    items: List<ContinueWatchingItem>,
    onItemClick: (ContinueWatchingItem) -> Unit,
    modifier: Modifier = Modifier,
    onItemFocused: (itemIndex: Int) -> Unit = {}
) {
    if (items.isEmpty()) return

    val listState = rememberLazyListState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Continue Watching",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = listState
        ) {
            itemsIndexed(items) { index, progress ->
                ContinueWatchingCard(
                    item = progress,
                    onClick = { onItemClick(progress) },
                    modifier = Modifier.onFocusChanged { 
                        if (it.isFocused) onItemFocused(index)
                    }
                )
            }
        }
    }
}

@Composable
internal fun ContinueWatchingCard(
    item: ContinueWatchingItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 288.dp,
    imageHeight: Dp = 162.dp
) {
    var isFocused by remember { mutableStateOf(false) }

    val progress = (item as? ContinueWatchingItem.InProgress)
    val nextUp = (item as? ContinueWatchingItem.NextUp)?.info
    
    val titleText = nextUp?.name ?: "Unknown"
    val imageModel = nextUp?.thumbnail ?: nextUp?.backdrop ?: nextUp?.poster

    Surface(
        onClick = onClick,
        modifier = modifier
            .width(cardWidth)
            .onFocusChanged { isFocused = it.isFocused },
        shape = CwCardShape,
        color = Color(0xFF1E1E1E),
        border = if (isFocused) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE91E63)) else null
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(CwClipShape)
            ) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = titleText,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.8f))))
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
