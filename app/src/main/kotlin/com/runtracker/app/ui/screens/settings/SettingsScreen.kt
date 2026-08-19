package com.runtracker.app.ui.screens.settings

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import com.runtracker.app.ui.components.SectionTitle
import com.runtracker.app.ui.theme.*
import com.runtracker.app.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val userWeight by viewModel.userWeight.collectAsState()
    val isMetric by viewModel.isMetric.collectAsState()
    val targetDistance by viewModel.targetDistance.collectAsState()
    val targetCalories by viewModel.targetCalories.collectAsState()
    val voiceEnabled by viewModel.voiceAnnouncement.collectAsState()

    var weightInput by remember(userWeight) { mutableStateOf(userWeight.toString()) }
    var targetDistInput by remember(targetDistance) {
        mutableStateOf(if (targetDistance > 0) targetDistance.toString() else "")
    }
    var targetCalInput by remember(targetCalories) {
        mutableStateOf(if (targetCalories > 0) targetCalories.toString() else "")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold, color = TextPrimary) },
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
            SectionTitle("Profil")

            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Berat Badan (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ClaudeOrange,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = ClaudeOrange,
                    cursorColor = ClaudeOrange
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Satuan", color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text(if (isMetric) "Kilometer" else "Miles", color = TextSecondary, fontSize = Dimensions.text_sm)
                }
                Switch(
                    checked = isMetric,
                    onCheckedChange = { viewModel.setMetric(it) },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = ClaudeOrange,
                        checkedThumbColor = Color.White,
                        uncheckedTrackColor = DividerColor
                    )
                )
            }

            HorizontalDivider(color = DividerColor)

            SectionTitle("Target Harian")

            OutlinedTextField(
                value = targetDistInput,
                onValueChange = { targetDistInput = it },
                label = { Text("Target Jarak (${if (isMetric) "km" else "mil"})") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ClaudeOrange,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = ClaudeOrange,
                    cursorColor = ClaudeOrange
                )
            )

            OutlinedTextField(
                value = targetCalInput,
                onValueChange = { targetCalInput = it },
                label = { Text("Target Kalori (kk)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ClaudeOrange,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = ClaudeOrange,
                    cursorColor = ClaudeOrange
                )
            )

            HorizontalDivider(color = DividerColor)

            SectionTitle("Suara")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Pengumuman Suara", color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Setiap 1 km", color = TextSecondary, fontSize = Dimensions.text_sm)
                }
                Switch(
                    checked = voiceEnabled,
                    onCheckedChange = { viewModel.setVoiceAnnouncement(it) },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = ClaudeOrange,
                        checkedThumbColor = Color.White,
                        uncheckedTrackColor = DividerColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimensions.spacing_lg))

            Button(
                onClick = {
                    val weight = weightInput.toDoubleOrNull() ?: 70.0
                    val dist = targetDistInput.toDoubleOrNull() ?: 0.0
                    val cal = targetCalInput.toDoubleOrNull() ?: 0.0
                    viewModel.saveSettings(weight, dist, cal)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.button_height),
                colors = ButtonDefaults.buttonColors(containerColor = ClaudeOrange),
                shape = RoundedCornerShape(Dimensions.radius_xxl),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl, color = Color.White)
            }
        }
    }
}
