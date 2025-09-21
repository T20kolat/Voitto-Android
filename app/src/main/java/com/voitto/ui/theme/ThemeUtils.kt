package com.voitto.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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

// Responsive card elevation
@Composable
fun getResponsiveCardElevation(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 2.dp
        ScreenSize.Medium -> 4.dp
        ScreenSize.Expanded -> 6.dp
    }
}

// Responsive icon size
@Composable
fun getResponsiveIconSize(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 24.dp
        ScreenSize.Medium -> 28.dp
        ScreenSize.Expanded -> 32.dp
    }
}

// Responsive button height
@Composable
fun getResponsiveButtonHeight(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 40.dp
        ScreenSize.Medium -> 48.dp
        ScreenSize.Expanded -> 56.dp
    }
}

// Responsive card padding
@Composable
fun getResponsiveCardPadding(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 16.dp
        ScreenSize.Medium -> 20.dp
        ScreenSize.Expanded -> 24.dp
    }
}

// Responsive content width (for larger screens)
@Composable
fun getResponsiveContentWidth(): Dp {
    return when (getScreenSize()) {
        ScreenSize.Compact -> Dp.Unspecified
        ScreenSize.Medium -> 600.dp
        ScreenSize.Expanded -> 800.dp
    }
}

// Responsive grid columns
@Composable
fun getResponsiveGridColumns(): Int {
    return when (getScreenSize()) {
        ScreenSize.Compact -> 1
        ScreenSize.Medium -> 2
        ScreenSize.Expanded -> 3
    }
}

// Responsive horizontal arrangement
@Composable
fun getResponsiveHorizontalArrangement(): Arrangement.Horizontal {
    return when (getScreenSize()) {
        ScreenSize.Compact -> Arrangement.spacedBy(8.dp)
        ScreenSize.Medium -> Arrangement.spacedBy(12.dp)
        ScreenSize.Expanded -> Arrangement.spacedBy(16.dp)
    }
}