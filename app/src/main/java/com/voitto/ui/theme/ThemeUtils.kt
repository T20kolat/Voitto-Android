package com.voitto.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Screen size breakpoints
object ScreenSizes {
    val Compact: Dp = 600.dp
    val Medium: Dp = 840.dp
    val Expanded: Dp = 1200.dp
}

// Screen size detection
@Composable
fun getScreenSize(): ScreenSize {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    return when {
        screenWidth < ScreenSizes.Compact -> ScreenSize.Compact
        screenWidth < ScreenSizes.Medium -> ScreenSize.Medium
        else -> ScreenSize.Expanded
    }
}

enum class ScreenSize {
    Compact,    // Phone portrait
    Medium,     // Phone landscape / small tablet
    Expanded    // Large tablet / desktop
}

// Theme-aware color utilities
@Composable
fun getIncomeColor(): Color {
    return if (isSystemInDarkTheme()) IncomeGreenDark else IncomeGreenLight
}

@Composable
fun getExpenseColor(): Color {
    return if (isSystemInDarkTheme()) ExpenseRedDark else ExpenseRedLight
}

@Composable
fun getWarningColor(): Color {
    return if (isSystemInDarkTheme()) WarningOrangeDark else WarningOrangeLight
}

@Composable
fun getSafeColor(): Color {
    return if (isSystemInDarkTheme()) SafeBlueDark else SafeBlueLight
}

@Composable
fun getSuccessColor(): Color {
    return if (isSystemInDarkTheme()) SuccessGreenDark else SuccessGreenLight
}

@Composable
fun getPositiveColor(): Color {
    return if (isSystemInDarkTheme()) PositiveBlueDark else PositiveBlueLight
}

// Responsive padding utilities
@Composable
fun getResponsivePadding(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 16.dp
        ScreenSize.Medium -> 24.dp
        ScreenSize.Expanded -> 32.dp
    }
}

@Composable
fun getResponsiveSpacing(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 8.dp
        ScreenSize.Medium -> 12.dp
        ScreenSize.Expanded -> 16.dp
    }
}

// Responsive typography scaling
@Composable
fun getResponsiveTextScale(): Float {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 1.0f
        ScreenSize.Medium -> 1.1f
        ScreenSize.Expanded -> 1.2f
    }
}
