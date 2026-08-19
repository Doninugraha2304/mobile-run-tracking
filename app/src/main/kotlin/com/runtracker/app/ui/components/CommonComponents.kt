package com.runtracker.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.runtracker.app.ui.theme.*

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        shape = RoundedCornerShape(Dimensions.radius_lg),
        border = BorderStroke(1.dp, DividerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacing_lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(Dimensions.radius_md),
                color = ClaudeOrangeLight,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spacing_sm))
            Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xl)
            Text(label, color = TextSecondary, fontSize = Dimensions.text_sm)
        }
    }
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(Dimensions.button_height),
        colors = ButtonDefaults.buttonColors(containerColor = ClaudeOrange),
        shape = RoundedCornerShape(Dimensions.radius_xl),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = Dimensions.text_xxl, color = Color.White)
    }
}

@Composable
fun DangerButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(Dimensions.button_height),
        colors = ButtonDefaults.buttonColors(containerColor = ClaudeRed),
        shape = RoundedCornerShape(Dimensions.radius_xl),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        icon?.invoke()
        if (icon != null) Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = Dimensions.text_xxl, color = Color.White)
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = Dimensions.text_xxxl
    )
}

@Composable
fun RealtimeStat(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(Dimensions.radius_sm),
            color = ClaudeOrangeLight,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = ClaudeOrange, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.spacing_xs))
        Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xl)
        Text(label, color = TextSecondary, fontSize = Dimensions.text_sm)
    }
}
