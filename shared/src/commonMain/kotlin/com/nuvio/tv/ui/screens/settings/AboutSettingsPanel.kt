package com.nuvio.tv.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nuvio.tv.ui.theme.NuvioColors

@Composable
internal fun AboutSettingsPanel() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SettingsDetailHeader("About", "Version and policies")
        
        SettingsGroupCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // App Info Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Nuvio Media Hub",
                            style = MaterialTheme.typography.headlineMedium,
                            color = NuvioColors.TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "Version 0.3.6 (Desktop)",
                            style = MaterialTheme.typography.bodyLarge,
                            color = NuvioColors.TextSecondary
                        )
                        
                        Text(
                            text = "A Kotlin Multiplatform media hub for all your needs.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NuvioColors.TextSecondary
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    SettingsButton(
                        text = "Check for Updates",
                        onClick = { /* TODO: Implement update check */ },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp)
                    )
                }

                // Links Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Links",
                        style = MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "Privacy Policy",
                        subtitle = "View our privacy policy",
                        onClick = { /* TODO: Open privacy policy URL */ }
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "Terms of Service",
                        subtitle = "View terms and conditions",
                        onClick = { /* TODO: Open terms URL */ }
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "GitHub Repository",
                        subtitle = "Visit the source code",
                        onClick = { /* TODO: Open GitHub URL */ }
                    )
                }
                
                item {
                    SettingsActionRow(
                        title = "Report an Issue",
                        subtitle = "Submit bug reports and feature requests",
                        onClick = { /* TODO: Open GitHub issues URL */ }
                    )
                }

                // Credits Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Credits",
                        style = MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Developed by the Nuvio team",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NuvioColors.TextSecondary
                        )
                        
                        Text(
                            text = "Desktop port by JaidenGoode",
                            style = MaterialTheme.typography.bodySmall,
                            color = NuvioColors.TextTertiary
                        )
                    }
                }
                
                // License Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "License",
                        style = MaterialTheme.typography.titleMedium,
                        color = NuvioColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
                
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Open Source Software",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NuvioColors.TextSecondary
                        )
                        
                        Text(
                            text = "Licensed under applicable open source licenses",
                            style = MaterialTheme.typography.bodySmall,
                            color = NuvioColors.TextTertiary
                        )
                    }
                }
            }
        }
    }
}
