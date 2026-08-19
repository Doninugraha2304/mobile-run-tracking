package com.runtracker.app.ui.screens.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Statistik", fontWeight = FontWeight.Bold, color = AccentGreen)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = AccentGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Minggu Ini",
                color = AccentGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            StatsCard(
                distance = weeklyDistance,
                calories = weeklyCalories,
                count = weeklyCount,
                duration = weeklyDuration
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Bulan Ini",
                color = AccentGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            StatsCard(
                distance = monthlyDistance,
                calories = monthlyCalories,
                count = monthlyCount,
                duration = monthlyDuration
            )
        }
    }
}

@Composable
fun StatsCard(
    distance: Double,
    calories: Double,
    count: Int,
    duration: Long
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.DirectionsRun,
                    value = LocationUtils.formatDistance(distance),
                    label = "Jarak"
                )
                StatItem(
                    icon = Icons.Default.LocalFireDepartment,
                    value = String.format("%.0f", calories),
                    label = "Kalori (kk)"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.FitnessCenter,
                    value = "$count",
                    label = "Jumlah Lari"
                )
                StatItem(
                    icon = Icons.Default.Timer,
                    value = LocationUtils.formatDuration(duration),
                    label = "Durasi Total"
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentGreen,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            value,
            color = AccentGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            label,
            color = LightGray,
            fontSize = 13.sp
        )
    }
}
