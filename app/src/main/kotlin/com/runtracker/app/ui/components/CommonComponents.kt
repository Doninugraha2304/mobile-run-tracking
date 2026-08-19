package com.runtracker.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        shape = RoundedCornerShape(Dimensions.radius_md)
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.card_padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentGreen,
                modifier = Modifier.size(Dimensions.icon_lg)
            )
            Spacer(modifier = Modifier.height(Dimensions.spacing_sm))
            Text(
                value,
                color = AccentGreen,
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.text_xl
            )
            Text(
                label,
                color = LightGray,
                fontSize = Dimensions.text_sm
            )
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
        colors = ButtonDefaults.buttonColors(containerColor = Green600),
        shape = RoundedCornerShape(Dimensions.radius_xxl),
        enabled = enabled
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl)
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
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        shape = RoundedCornerShape(Dimensions.radius_xxl)
    ) {
        icon?.invoke()
        if (icon != null) Spacer(modifier = Modifier.width(Dimensions.spacing_sm))
        Text(text, fontWeight = FontWeight.Bold, fontSize = Dimensions.text_xxl)
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        color = AccentGreen,
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
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentGreen,
            modifier = Modifier.size(Dimensions.icon_md)
        )
        Spacer(modifier = Modifier.height(Dimensions.spacing_xs))
        Text(
            value,
            color = AccentGreen,
            fontWeight = FontWeight.Bold,
            fontSize = Dimensions.text_xl
        )
        Text(
            label,
            color = LightGray,
            fontSize = Dimensions.text_sm
        )
    }
}
