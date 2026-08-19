package com.runtracker.app.ui.screens.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.ui.components.StatCard
import com.runtracker.app.ui.components.SectionTitle
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
            CenterAlignedTopAppBar(
                title = {
                    Text("RunTracker", fontWeight = FontWeight.Bold, color = TextPrimary)
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceLight)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(Dimensions.screen_padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Dimensions.spacing_xl))

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(colors = listOf(RunningGradientStart, RunningGradientEnd))
                    )
                    .clickable { onStartRun() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Mulai Lari",
                        tint = Color.White,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("MULAI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl)
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xl))

            OutlinedButton(
                onClick = onStartInterval,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ClaudeOrange),
                border = BorderStroke(1.5.dp, ClaudeOrange),
                shape = RoundedCornerShape(Dimensions.radius_xl)
            ) {
                Icon(Icons.Default.Repeat, contentDescription = null, modifier = Modifier.size(Dimensions.icon_md))
                Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
                Text("Interval Training", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xxl))

            SectionTitle("Minggu Ini")
            Spacer(modifier = Modifier.height(Dimensions.spacing_md))

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
                StatCard(modifier = Modifier.weight(1f), label = "Lari", value = "${viewModel.weeklyCount.value}x", icon = Icons.Default.History)
                StatCard(modifier = Modifier.weight(1f), label = "Durasi", value = LocationUtils.formatDuration(viewModel.weeklyDuration.value), icon = Icons.Default.Timer)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xxl))

            SectionTitle("Rekor Terbaik")
            Spacer(modifier = Modifier.height(Dimensions.spacing_md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing_md)
            ) {
                StatCard(modifier = Modifier.weight(1f), label = "Terjauh", value = LocationUtils.formatDistance(viewModel.bestDistance.value), icon = Icons.Default.EmojiEvents)
                StatCard(modifier = Modifier.weight(1f), label = "Terbaik", value = LocationUtils.formatPace(viewModel.bestPace.value), icon = Icons.Default.Speed)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_xxl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing_md)
            ) {
                NavigationCard(modifier = Modifier.weight(1f), label = "Riwayat", icon = Icons.Default.History, onClick = onHistory)
                NavigationCard(modifier = Modifier.weight(1f), label = "Statistik", icon = Icons.Default.BarChart, onClick = onStats)
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_lg))
        }
    }
}

@Composable
fun NavigationCard(modifier: Modifier = Modifier, label: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        border = BorderStroke(1.dp, DividerColor),
        shape = RoundedCornerShape(Dimensions.radius_lg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimensions.spacing_lg),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(Dimensions.icon_md))
            Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
            Text(label, color = TextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}
