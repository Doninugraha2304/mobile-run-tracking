package com.runtracker.app.ui.screens.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.data.db.RunEntity
import com.runtracker.app.ui.theme.*
import com.runtracker.app.util.LocationUtils
import com.runtracker.app.viewmodel.RunViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: RunViewModel = hiltViewModel()
) {
    val runs by viewModel.allRuns.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Riwayat Lari", fontWeight = FontWeight.Bold, color = TextPrimary) },
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
        if (runs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(Dimensions.spacing_lg))
                    Text("Belum ada riwayat lari", color = TextSecondary, fontSize = Dimensions.text_xl)
                    Text("Mulai lari pertamamu!", color = TextTertiary, fontSize = Dimensions.text_lg)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = Dimensions.spacing_lg),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing_md),
                contentPadding = PaddingValues(vertical = Dimensions.spacing_lg)
            ) {
                items(runs) { run -> RunHistoryCard(run = run) }
            }
        }
    }
}

@Composable
fun RunHistoryCard(run: RunEntity) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        border = BorderStroke(1.dp, DividerColor),
        shape = RoundedCornerShape(Dimensions.radius_lg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(Dimensions.spacing_lg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(Dimensions.spacing_xs))
                Text(dateFormat.format(Date(run.endTime)), color = TextSecondary, fontSize = Dimensions.text_sm)
                if (run.isInterval) {
                    Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
                    Surface(shape = RoundedCornerShape(Dimensions.spacing_xxs), color = ClaudeOrangeLight) {
                        Text("Interval", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = ClaudeOrange, fontSize = Dimensions.text_xs, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RunStatItem(icon = Icons.Default.DirectionsRun, value = LocationUtils.formatDistance(run.distance), label = "Jarak")
                RunStatItem(icon = Icons.Default.Speed, value = LocationUtils.formatSpeed(run.avgSpeed), label = "Kecepatan")
                RunStatItem(icon = Icons.Default.LocalFireDepartment, value = String.format("%.0f kk", run.calories), label = "Kalori")
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Durasi: ${LocationUtils.formatDuration(run.duration)}", color = TextTertiary, fontSize = Dimensions.text_sm)
                Spacer(modifier = Modifier.width(Dimensions.spacing_lg))
                Text("Pace: ${LocationUtils.formatPace(run.avgPace)}", color = TextTertiary, fontSize = Dimensions.text_sm)
            }
        }
    }
}

@Composable
fun RunStatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = RoundedCornerShape(Dimensions.radius_sm), color = ClaudeOrangeLight, modifier = Modifier.size(28.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.spacing_xs))
        Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_lg)
        Text(label, color = TextTertiary, fontSize = Dimensions.text_xs)
    }
}
