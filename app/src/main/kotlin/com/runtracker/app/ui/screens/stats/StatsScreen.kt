package com.runtracker.app.ui.screens.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.ui.components.SectionTitle
import com.runtracker.app.ui.theme.*
import com.runtracker.app.util.LocationUtils
import com.runtracker.app.viewmodel.RunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onBack: () -> Unit,
    viewModel: RunViewModel = hiltViewModel()
) {
    val weeklyDistance by viewModel.weeklyDistance.collectAsState()
    val weeklyCalories by viewModel.weeklyCalories.collectAsState()
    val weeklyCount by viewModel.weeklyCount.collectAsState()
    val weeklyDuration by viewModel.weeklyDuration.collectAsState()
    val monthlyDistance by viewModel.monthlyDistance.collectAsState()
    val monthlyCalories by viewModel.monthlyCalories.collectAsState()
    val monthlyCount by viewModel.monthlyCount.collectAsState()
    val monthlyDuration by viewModel.monthlyDuration.collectAsState()
    val totalRuns by viewModel.totalRuns.collectAsState()
    val totalAllDistance by viewModel.totalAllDistance.collectAsState()
    val totalAllCalories by viewModel.totalAllCalories.collectAsState()
    val bestDistance by viewModel.bestDistance.collectAsState()
    val bestPace by viewModel.bestPace.collectAsState()
    val bestSpeed by viewModel.bestSpeed.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Statistik", fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
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
                .padding(Dimensions.spacing_lg),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing_xxl)
        ) {
            SectionTitle("Minggu Ini")
            StatsSummaryCard(weeklyDistance, weeklyCalories, weeklyCount, weeklyDuration)

            SectionTitle("Bulan Ini")
            StatsSummaryCard(monthlyDistance, monthlyCalories, monthlyCount, monthlyDuration)

            SectionTitle("Total Semua")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                border = BorderStroke(1.dp, DividerColor),
                shape = RoundedCornerShape(Dimensions.radius_lg),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(Dimensions.card_padding)) {
                    StatsRow(
                        items = listOf(
                            StatsItemData(Icons.Default.DirectionsRun, "$totalRuns", "Total Lari"),
                            StatsItemData(Icons.Default.EmojiEvents, LocationUtils.formatDistance(totalAllDistance), "Total Jarak")
                        )
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacing_lg))
                    StatsRow(
                        items = listOf(
                            StatsItemData(Icons.Default.LocalFireDepartment, String.format("%.0f kk", totalAllCalories), "Total Kalori"),
                            StatsItemData(Icons.Default.Speed, LocationUtils.formatPace(bestPace), "Pace Terbaik")
                        )
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacing_lg))
                    StatsRow(
                        items = listOf(
                            StatsItemData(Icons.Default.EmojiEvents, LocationUtils.formatDistance(bestDistance), "Jarak Terjauh"),
                            StatsItemData(Icons.Default.Speed, LocationUtils.formatSpeed(bestSpeed), "Kecepatan Tertinggi")
                        )
                    )
                }
            }
        }
    }
}

data class StatsItemData(
    val icon: ImageVector,
    val value: String,
    val label: String
)

@Composable
fun StatsSummaryCard(distance: Double, calories: Double, count: Int, duration: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        border = BorderStroke(1.dp, DividerColor),
        shape = RoundedCornerShape(Dimensions.radius_lg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(Dimensions.card_padding)) {
            StatsRow(
                items = listOf(
                    StatsItemData(Icons.Default.DirectionsRun, LocationUtils.formatDistance(distance), "Jarak"),
                    StatsItemData(Icons.Default.LocalFireDepartment, String.format("%.0f", calories), "Kalori (kk)")
                )
            )
            Spacer(modifier = Modifier.height(Dimensions.spacing_lg))
            StatsRow(
                items = listOf(
                    StatsItemData(Icons.Default.FitnessCenter, "$count", "Jumlah Lari"),
                    StatsItemData(Icons.Default.Timer, LocationUtils.formatDuration(duration), "Durasi Total")
                )
            )
        }
    }
}

@Composable
fun StatsRow(items: List<StatsItemData>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { item ->
            StatsItem(icon = item.icon, value = item.value, label = item.label)
        }
    }
}

@Composable
fun StatsItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = RoundedCornerShape(Dimensions.radius_sm), color = ClaudeOrangeLight, modifier = Modifier.size(36.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.spacing_sm))
        Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl)
        Text(label, color = TextSecondary, fontSize = Dimensions.text_md)
    }
}
