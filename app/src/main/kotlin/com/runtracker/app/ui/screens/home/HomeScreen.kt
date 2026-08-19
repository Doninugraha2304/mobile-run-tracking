package com.runtracker.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.ui.components.StatCard
import com.runtracker.app.ui.theme.*
import com.runtracker.app.util.LocationUtils
import com.runtracker.app.viewmodel.RunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartRun: () -> Unit,
    onStartInterval: () -> Unit,
    onHistory: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
    viewModel: RunViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RunTracker", fontWeight = FontWeight.Bold, color = AccentGreen) },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = AccentGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(Dimensions.screen_padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Dimensions.spacing_xxxxl))

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(colors = listOf(Green600, Green800)))
                    .clickable { onStartRun() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.DirectionsRun,
                        contentDescription = "Mulai Lari",
                        tint = AccentGreen,
                        modifier = Modifier.size(Dimensions.icon_xxl)
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacing_sm))
                    Text("MULAI", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl)
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xl))

            OutlinedButton(
                onClick = onStartInterval,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentGreen),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true)
            ) {
                Icon(Icons.Default.Repeat, contentDescription = null, modifier = Modifier.size(Dimensions.icon_md))
                Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
                Text("Interval Training", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xxl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing_md)
            ) {
                StatCard(modifier = Modifier.weight(1f), label = "Jarak", value = LocationUtils.formatDistance(viewModel.weeklyDistance.value), icon = Icons.Default.DirectionsRun)
                StatCard(modifier = Modifier.weight(1f), label = "Kalori", value = String.format("%.0f kk", viewModel.weeklyCalories.value), icon = Icons.Default.LocalFireDepartment)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing_md)
            ) {
                StatCard(modifier = Modifier.weight(1f), label = "Jumlah Lari", value = "${viewModel.weeklyCount.value} kali", icon = Icons.Default.History)
                StatCard(modifier = Modifier.weight(1f), label = "Durasi", value = LocationUtils.formatDuration(viewModel.weeklyDuration.value), icon = Icons.Default.Timer)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xxl))

            Text("Rekor Terbaik", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxxl)

            Spacer(modifier = Modifier.height(Dimensions.spacing_md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing_md)
            ) {
                StatCard(modifier = Modifier.weight(1f), label = "Jarak Terjauh", value = LocationUtils.formatDistance(viewModel.bestDistance.value), icon = Icons.Default.EmojiEvents)
                StatCard(modifier = Modifier.weight(1f), label = "Pace Terbaik", value = LocationUtils.formatPace(viewModel.bestPace.value), icon = Icons.Default.Speed)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xxl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing_md)
            ) {
                BottomButton(modifier = Modifier.weight(1f), label = "Riwayat", icon = Icons.Default.History, onClick = onHistory)
                BottomButton(modifier = Modifier.weight(1f), label = "Statistik", icon = Icons.Default.BarChart, onClick = onStats)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_lg))
        }
    }
}

@Composable
fun BottomButton(modifier: Modifier = Modifier, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Green600),
        shape = RoundedCornerShape(Dimensions.radius_md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimensions.card_padding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(Dimensions.icon_md))
            Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
            Text(label, color = AccentGreen, fontWeight = FontWeight.Bold)
        }
    }
}
