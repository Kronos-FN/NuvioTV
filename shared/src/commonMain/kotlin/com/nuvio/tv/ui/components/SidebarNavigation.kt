package com.nuvio.tv.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nuvio.tv.ui.theme.NuvioColors

private val NavItemShape = RoundedCornerShape(14.dp)
private val NavItemIconShape = RoundedCornerShape(10.dp)

enum class NavigationItem(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    Search("Search", Icons.Default.Search),
    Library("Library", Icons.Default.Favorite),
    Addons("Addons", Icons.Default.Add),
    Settings("Settings", Icons.Default.Settings)
}

@Composable
fun SidebarNavigation(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }
    val width by animateDpAsState(if (isHovered) 260.dp else 80.dp)

    Column(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(NuvioColors.BackgroundElevated)
            .padding(vertical = 24.dp, horizontal = 12.dp)
            .onFocusChanged { isHovered = it.hasFocus },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (isHovered) 12.dp else 0.dp),
            contentAlignment = if (isHovered) Alignment.CenterStart else Alignment.Center
        ) {
            Text(
                text = if (isHovered) "NUVIO" else "N",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = NuvioColors.Secondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        NavigationItem.entries.forEach { item ->
            SidebarNavItem(
                item = item,
                isSelected = selectedItem == item,
                isExpanded = isHovered,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
private fun SidebarNavItem(
    item: NavigationItem,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        if (isFocused || isSelected) NuvioColors.FocusBackground else Color.Transparent
    )
    val borderColor by animateColorAsState(
        if (isFocused) NuvioColors.FocusRing else Color.Transparent
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(NavItemShape)
            .background(backgroundColor)
            .border(width = 2.dp, color = borderColor, shape = NavItemShape)
            .onFocusChanged { isFocused = it.isFocused }
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(NavItemIconShape)
                .background(NuvioColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isFocused || isSelected) NuvioColors.Secondary else NuvioColors.TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.label,
                style = MaterialTheme.typography.titleMedium,
                color = if (isFocused || isSelected) NuvioColors.TextPrimary else NuvioColors.TextSecondary,
                maxLines = 1
            )
        }
    }
}
