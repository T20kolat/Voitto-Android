package com.voitto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voitto.ui.components.ResponsiveCard
import com.voitto.ui.components.ResponsiveContainer
import com.voitto.ui.components.ResponsiveGrid
import com.voitto.ui.theme.getResponsivePadding
import com.voitto.ui.theme.getResponsiveSpacing
import com.voitto.ui.theme.getScreenSize
import com.voitto.ui.theme.ScreenSize

@Composable
fun ResponsiveDemoScreen(
    modifier: Modifier = Modifier
) {
    ResponsiveContainer(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(getResponsivePadding()),
            verticalArrangement = Arrangement.spacedBy(getResponsiveSpacing())
        ) {
            item {
                ResponsiveCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Responsive Design Demo",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Current screen size: ${getScreenSize()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            item {
                ResponsiveCard {
                    Column {
                        Text(
                            text = "Responsive Grid Example",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ResponsiveGrid {
                            repeat(6) { index ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Item ${index + 1}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                ResponsiveCard {
                    Column {
                        Text(
                            text = "Responsive Typography",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Display Large",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "Headline Medium",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Title Large",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Body Large",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Label Small",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            item {
                ResponsiveCard {
                    Column {
                        Text(
                            text = "Responsive Spacing & Sizing",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { /* Demo button */ }
                            ) {
                                Text("Small")
                            }
                            Button(
                                onClick = { /* Demo button */ }
                            ) {
                                Text("Medium")
                            }
                            Button(
                                onClick = { /* Demo button */ }
                            ) {
                                Text("Large")
                            }
                        }
                    }
                }
            }
        }
    }
}
