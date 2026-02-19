package com.nuvio.tv.ui.screens.addon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuvio.tv.domain.model.Addon
import com.nuvio.tv.ui.screens.home.HomeViewModel

@Composable
fun AddonManagerScreen(
    viewModel: HomeViewModel // Reusing for now to show installed addons
) {
    var installUrl by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        Text("Manage Addons", style = MaterialTheme.typography.headlineLarge, color = Color.White)
        
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = installUrl,
                onValueChange = { installUrl = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("https://example.com/manifest.json", color = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* Install logic */ }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Install")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Installed Addons", style = MaterialTheme.typography.titleLarge, color = Color.White)
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // This is a placeholder since HomeViewModel doesn't expose all addons directly in uiState yet
            // In a real scenario, we'd have an AddonViewModel
            item {
                AddonItemPlaceholder("Cinemeta", "https://v3-cinemeta.strem.io/manifest.json")
            }
        }
    }
}

@Composable
fun AddonItemPlaceholder(name: String, url: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(url, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            IconButton(onClick = { /* Remove */ }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}
