package com.voitto.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voitto.ui.theme.SafeBlue
import com.voitto.ui.theme.SuccessGreen
import kotlinx.coroutines.delay

@Composable
fun AnimatedBalance(
    label: String,
    amount: String,
    isPositive: Boolean = true,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    val animatedAmount by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(800, delayMillis = 200),
        label = "balance_animation"
    )
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedAmount),
        colors = CardDefaults.cardColors(
            containerColor = if (isPositive) SuccessGreen.copy(alpha = 0.1f) else SafeBlue.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = if (isPositive) SuccessGreen else SafeBlue
            )
        }
    }
}
