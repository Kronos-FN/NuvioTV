package com.nuvio.tv.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nuvio.tv.ui.theme.NuvioColors

internal val SettingsContainerRadius = 28.dp
internal val SettingsPillRadius = 999.dp
internal val SettingsSecondaryCardRadius = 18.dp
internal val SettingsRailItemHeight = 56.dp

@Composable
internal fun SettingsWorkspaceSurface(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(SettingsContainerRadius))
            .background(NuvioColors.BackgroundElevated)
            .border(
                width = 1.dp,
                color = NuvioColors.Border,
                shape = RoundedCornerShape(SettingsContainerRadius)
            )
            .padding(20.dp),
        content = content
    )
}

@Composable
internal fun SettingsRailButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    onFocused: () -> Unit = {},
    icon: ImageVector? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = SettingsRailItemHeight)
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) onFocused()
            }
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        color = if (isSelected || isFocused) NuvioColors.BackgroundCard else Color.Transparent,
        border = if (isSelected || isFocused) BorderStroke(1.dp, NuvioColors.FocusRing) else null,
        shape = RoundedCornerShape(SettingsPillRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected || isFocused) NuvioColors.TextPrimary else NuvioColors.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isSelected || isFocused) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isSelected || isFocused) NuvioColors.TextPrimary else NuvioColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = NuvioColors.TextTertiary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
internal fun SettingsDetailHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = NuvioColors.TextPrimary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = NuvioColors.TextSecondary
        )
    }
}

@Composable
internal fun SettingsGroupCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SettingsSecondaryCardRadius))
            .background(NuvioColors.BackgroundCard)
            .border(
                width = 1.dp,
                color = NuvioColors.Border,
                shape = RoundedCornerShape(SettingsSecondaryCardRadius)
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (!title.isNullOrBlank()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = NuvioColors.TextPrimary
            )
        }
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = NuvioColors.TextSecondary
            )
        }
        content()
    }
}

@Composable
internal fun SettingsActionRow(
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 62.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(SettingsPillRadius)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = NuvioColors.TextPrimary)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = NuvioColors.TextSecondary)
                }
            }
            if (trailingContent != null) {
                trailingContent()
            } else {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = NuvioColors.TextTertiary)
            }
        }
    }
}

@Composable
internal fun SettingsToggleRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 62.dp)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = NuvioColors.TextPrimary)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = NuvioColors.TextSecondary)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NuvioColors.Accent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

@Composable
internal fun SettingsSliderRow(
    title: String,
    subtitle: String? = null,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    modifier: Modifier = Modifier,
    valueLabel: (Float) -> String = { it.toInt().toString() }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = NuvioColors.TextPrimary)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = NuvioColors.TextSecondary)
                }
            }
            Text(
                text = valueLabel(value),
                style = MaterialTheme.typography.bodyMedium,
                color = NuvioColors.Accent,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = NuvioColors.Accent,
                activeTrackColor = NuvioColors.Accent,
                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
internal fun SettingsChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        color = if (selected) NuvioColors.Accent else NuvioColors.BackgroundElevated,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (selected) NuvioColors.Accent else NuvioColors.Border)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) Color.White else NuvioColors.TextPrimary,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
internal fun SettingsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = NuvioColors.Accent,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
