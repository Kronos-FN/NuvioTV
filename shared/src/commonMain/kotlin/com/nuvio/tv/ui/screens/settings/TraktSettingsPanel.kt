package com.nuvio.tv.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.TraktConnectionMode
import com.nuvio.tv.ui.theme.NuvioColors
import com.nuvio.tv.ui.viewmodel.TraktViewModel

@Composable
internal fun TraktSettingsPanel(
    viewModel: TraktViewModel
) {
    val preferences by viewModel.preferences.collectAsState()
    val connectionMode by viewModel.connectionMode.collectAsState()
    val deviceCode by viewModel.deviceCode.collectAsState()
    val isPolling by viewModel.isPolling.collectAsState()
    val error by viewModel.error.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SettingsDetailHeader("Trakt", "Connect your Trakt account for syncing and tracking")
        
        // Error display
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }
        
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            when (connectionMode) {
                TraktConnectionMode.DISCONNECTED -> TraktDisconnectedState(
                    onConnect = { viewModel.startDeviceCodeFlow() }
                )
                TraktConnectionMode.AWAITING_APPROVAL -> TraktAwaitingApprovalState(
                    deviceCode = deviceCode,
                    isPolling = isPolling,
                    onCancel = { 
                        viewModel.stopPolling()
                        viewModel.disconnect() 
                    }
                )
                TraktConnectionMode.CONNECTED -> TraktConnectedState(
                    preferences = preferences,
                    userProfile = userProfile,
                    viewModel = viewModel,
                    onDisconnect = { viewModel.disconnect() }
                )
            }
        }
    }
}

@Composable
private fun TraktDisconnectedState(
    onConnect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Not Connected",
            style = MaterialTheme.typography.titleLarge,
            color = NuvioColors.TextPrimary
        )
        
        Text(
            text = "Connect your Trakt account to sync your watch history, ratings, and watchlist across devices.",
            style = MaterialTheme.typography.bodyMedium,
            color = NuvioColors.TextSecondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SettingsButton(
            text = "Connect to Trakt",
            onClick = onConnect
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Features:",
            style = MaterialTheme.typography.titleSmall,
            color = NuvioColors.TextPrimary
        )
        
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BulletPoint("Sync watch progress across devices")
            BulletPoint("Rate and review content")
            BulletPoint("Manage watchlists")
            BulletPoint("Track viewing statistics")
        }
    }
}

@Composable
private fun TraktAwaitingApprovalState(
    deviceCode: com.nuvio.tv.domain.model.TraktDeviceCode?,
    isPolling: Boolean,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Awaiting Approval",
            style = MaterialTheme.typography.titleLarge,
            color = NuvioColors.TextPrimary
        )
        
        Text(
            text = "Visit the URL below and enter the code to authorize this app:",
            style = MaterialTheme.typography.bodyMedium,
            color = NuvioColors.TextSecondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (deviceCode != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = deviceCode.verificationUrl,
                    style = MaterialTheme.typography.bodyLarge,
                    color = NuvioColors.Secondary,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Code: ${deviceCode.userCode}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = NuvioColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isPolling) {
            androidx.compose.material3.CircularProgressIndicator(
                color = NuvioColors.Secondary
            )
            Text(
                text = "Waiting for authorization...",
                style = MaterialTheme.typography.bodySmall,
                color = NuvioColors.TextSecondary
            )
        } else {
            Text(
                text = "Polling stopped",
                style = MaterialTheme.typography.bodySmall,
                color = NuvioColors.TextSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SettingsButton(
            text = "Cancel",
            onClick = onCancel
        )
    }
}

@Composable
private fun TraktConnectedState(
    preferences: com.nuvio.tv.domain.model.TraktPreferences,
    userProfile: String?,
    viewModel: TraktViewModel,
    onDisconnect: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Connected",
                    style = MaterialTheme.typography.titleLarge,
                    color = NuvioColors.Secondary
                )
                
                if (userProfile != null) {
                    Text(
                        text = "Logged in as: $userProfile",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NuvioColors.TextPrimary
                    )
                } else if (preferences.username.isNotBlank()) {
                    Text(
                        text = "Logged in as: ${preferences.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NuvioColors.TextPrimary
                    )
                }
                
                val expiryDays = (preferences.expiresIn / (24 * 3600)).toInt()
                Text(
                    text = "Token expires in ~$expiryDays days",
                    style = MaterialTheme.typography.bodySmall,
                    color = NuvioColors.TextSecondary
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        item {
            SettingsSliderRow(
                title = "Continue Watching Days Cap",
                subtitle = "How many days of history to show",
                value = preferences.continueWatchingDaysCap.toFloat(),
                onValueChange = { viewModel.setContinueWatchingDaysCap(it.toInt()) },
                valueRange = 7f..90f,
                steps = 0,
                valueLabel = { "${it.toInt()} days" }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            SettingsButton(
                text = "Disconnect",
                onClick = onDisconnect,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp)
            )
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodyMedium,
            color = NuvioColors.TextSecondary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = NuvioColors.TextSecondary
        )
    }
}
