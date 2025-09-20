package com.voitto.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.voitto.ui.theme.getExpenseColor
import com.voitto.ui.theme.getIncomeColor
import kotlinx.coroutines.delay

@Composable
fun AnimatedTransactionBar(
    amount: Float,
    color: Color = if (amount >= 0) getIncomeColor() else getExpenseColor(),
    modifier: Modifier = Modifier
) {
    // Use remember to prevent recreation on recomposition
    var isVisible by remember { mutableStateOf(false) }
    
    // Single animation for both height and alpha to reduce overhead
    val animatedProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "bar_progress"
    )
    
    // Trigger animation only once
    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }
    
    Box(
        modifier = modifier
            .width(8.dp)
            .height((kotlin.math.abs(amount) / 2f * animatedProgress).dp.coerceAtLeast(8.dp))
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = animatedProgress))
    )
}
