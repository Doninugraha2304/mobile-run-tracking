package com.runtracker.app.ui.screens.interval

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.ui.components.SectionTitle
import com.runtracker.app.ui.theme.*
import com.runtracker.app.viewmodel.IntervalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalScreen(
    onBack: () -> Unit,
    onStartInterval: (runSec: Int, walkSec: Int, sets: Int) -> Unit,
    viewModel: IntervalViewModel = hiltViewModel()
) {
    val runTime by viewModel.runTime.collectAsState()
    val walkTime by viewModel.walkTime.collectAsState()
    val sets by viewModel.sets.collectAsState()

    var runInput by remember(runTime) { mutableStateOf(runTime.toString()) }
    var walkInput by remember(walkTime) { mutableStateOf(walkTime.toString()) }
    var setsInput by remember(sets) { mutableStateOf(sets.toString()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Interval Training", fontWeight = FontWeight.Bold, color = TextPrimary) },
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
                .padding(Dimensions.screen_padding),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing_xxl)
        ) {
            SectionTitle("Konfigurasi Interval")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                border = BorderStroke(1.dp, DividerColor),
                shape = RoundedCornerShape(Dimensions.radius_md),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(Dimensions.card_padding)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = Dimensions.spacing_sm)
                    ) {
                        Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = ClaudeRed, modifier = Modifier.size(Dimensions.icon_lg))
                        Spacer(modifier = Modifier.width(Dimensions.spacing_md))
                        Text("Lari", color = ClaudeRed, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = runInput,
                            onValueChange = { runInput = it },
                            modifier = Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            suffix = { Text("d", color = TextTertiary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ClaudeRed,
                                unfocusedBorderColor = BorderColor,
                                cursorColor = ClaudeRed
                            )
                        )
                    }

                    HorizontalDivider(color = DividerColor)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = Dimensions.spacing_sm)
                    ) {
                        Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = ClaudeGreen, modifier = Modifier.size(Dimensions.icon_lg))
                        Spacer(modifier = Modifier.width(Dimensions.spacing_md))
                        Text("Jalan", color = ClaudeGreen, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = walkInput,
                            onValueChange = { walkInput = it },
                            modifier = Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            suffix = { Text("d", color = TextTertiary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ClaudeGreen,
                                unfocusedBorderColor = BorderColor,
                                cursorColor = ClaudeGreen
                            )
                        )
                    }

                    HorizontalDivider(color = DividerColor)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = Dimensions.spacing_sm)
                    ) {
                        Icon(Icons.Default.Repeat, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(Dimensions.icon_lg))
                        Spacer(modifier = Modifier.width(Dimensions.spacing_md))
                        Text("Set", color = ClaudeOrange, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = setsInput,
                            onValueChange = { setsInput = it },
                            modifier = Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            suffix = { Text("x", color = TextTertiary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ClaudeOrange,
                                unfocusedBorderColor = BorderColor,
                                cursorColor = ClaudeOrange
                            )
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                border = BorderStroke(1.dp, DividerColor),
                shape = RoundedCornerShape(Dimensions.radius_md),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.card_padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Durasi", color = TextSecondary, fontSize = Dimensions.text_lg)
                    Spacer(modifier = Modifier.height(Dimensions.spacing_xs))
                    val run = runInput.toIntOrNull() ?: 5
                    val walk = walkInput.toIntOrNull() ?: 3
                    val s = setsInput.toIntOrNull() ?: 8
                    val totalSec = (run + walk) * s
                    val min = totalSec / 60
                    val sec = totalSec % 60
                    Text(
                        String.format("%d:%02d", min, sec),
                        color = ClaudeOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.text_timer
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.save(runInput.toIntOrNull() ?: 5, walkInput.toIntOrNull() ?: 3, setsInput.toIntOrNull() ?: 8)
                    onStartInterval(runInput.toIntOrNull() ?: 5, walkInput.toIntOrNull() ?: 3, setsInput.toIntOrNull() ?: 8)
                },
                modifier = Modifier.fillMaxWidth().height(Dimensions.button_height),
                colors = ButtonDefaults.buttonColors(containerColor = ClaudeOrange),
                shape = RoundedCornerShape(Dimensions.radius_xxl),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("MULAI INTERVAL", fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl, color = Color.White)
            }
        }
    }
}
