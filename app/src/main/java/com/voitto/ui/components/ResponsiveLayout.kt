package com.voitto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voitto.ui.theme.getResponsiveCardElevation
import com.voitto.ui.theme.getResponsiveCardPadding
import com.voitto.ui.theme.getResponsiveContentWidth
import com.voitto.ui.theme.getScreenSize
import com.voitto.ui.theme.ScreenSize

@Composable
fun ResponsiveCard(
    modifier: Modifier = Modifier,
    colors: androidx.compose.material3.CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = getResponsiveCardElevation()),
        colors = colors
    ) {
        Column(
            modifier = Modifier.padding(getResponsiveCardPadding()),
            content = content
        )
    }
}

@Composable
fun ResponsiveRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when (getScreenSize()) {
        ScreenSize.Compact -> {
            Column(modifier = modifier) {
                content()
            }
        }
        ScreenSize.Medium, ScreenSize.Expanded -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ResponsiveGrid(
    modifier: Modifier = Modifier,
    columns: Int = when (getScreenSize()) {
        ScreenSize.Compact -> 1
        ScreenSize.Medium -> 2
        ScreenSize.Expanded -> 3
    },
    content: @Composable () -> Unit
) {
    when (columns) {
        1 -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
        2 -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    content()
                }
            }
        }
        else -> {
            // For 3+ columns, use a more complex grid layout
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ResponsiveContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val maxWidth = getResponsiveContentWidth()
    val screenSize = getScreenSize()
    
    when (screenSize) {
        ScreenSize.Compact -> {
            // For phones, use full width
            Box(modifier = modifier.fillMaxSize()) {
                content()
            }
        }
        ScreenSize.Medium -> {
            // For tablets, center with max width
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = maxWidth)
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
        ScreenSize.Expanded -> {
            // For large screens, center with wider max width
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = maxWidth)
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}
