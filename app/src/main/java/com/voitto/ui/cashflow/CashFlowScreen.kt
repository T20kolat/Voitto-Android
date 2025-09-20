package com.voitto.ui.cashflow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitto.R
import com.voitto.ui.components.AnimatedBalance
import com.voitto.ui.components.AnimatedButton
import com.voitto.ui.components.AnimatedTransactionBar
import com.voitto.ui.components.LoadingShimmer
import com.voitto.ui.theme.IncomeGreen
import com.voitto.ui.theme.ExpenseRed
import com.voitto.ui.viewmodel.CashFlowViewModel
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@Composable
fun CashFlowScreen(
    modifier: Modifier = Modifier,
    viewModel: CashFlowViewModel = hiltViewModel()
) {
    var showCalendarView by remember { mutableStateOf(false) }
    val currentBalance by viewModel.currentBalance.collectAsState()
    val safeToSpendResult by viewModel.safeToSpendResult.collectAsState()
    val selectedWeek by viewModel.selectedWeek.collectAsState()
    val weeklyTransactions by viewModel.weeklyTransactions.collectAsState()
    
    val formatter = DecimalFormat("#,##0.00")
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance cards
        item {
            AnimatedBalance(
                label = stringResource(id = R.string.balance_now),
                amount = "€${formatter.format(currentBalance)}",
                isPositive = currentBalance >= 0
            )
        }
        
        item {
            AnimatedBalance(
                label = stringResource(id = R.string.safe_to_spend),
                amount = safeToSpendResult?.let { "€${formatter.format(it.safeToSpend)}" } ?: "€0,00",
                isPositive = (safeToSpendResult?.safeToSpend ?: 0f) >= 0
            )
        }

        // View toggle and navigation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showCalendarView) "30 päivän näkymä" else "Viikkonäkymä",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showCalendarView = false },
                                modifier = Modifier.height(32.dp),
                                colors = if (!showCalendarView) 
                                    androidx.compose.material3.ButtonDefaults.buttonColors() 
                                else 
                                    androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
                            ) {
                                Text("Viikko", style = MaterialTheme.typography.bodySmall)
                            }
                            Button(
                                onClick = { showCalendarView = true },
                                modifier = Modifier.height(32.dp),
                                colors = if (showCalendarView) 
                                    androidx.compose.material3.ButtonDefaults.buttonColors() 
                                else 
                                    androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
                            ) {
                                Text("30pv", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    
                    if (!showCalendarView) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { viewModel.selectPreviousWeek() },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text("←")
                            }
                            
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${selectedWeek.format(dateFormatter)} - ${selectedWeek.plusDays(6).format(dateFormatter)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                                Text(
                                    text = "Viikko ${selectedWeek.dayOfYear / 7 + 1}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Button(
                                onClick = { viewModel.selectNextWeek() },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text("→")
                            }
                        }
                    }
                }
            }
        }

        // Daily transactions with staggered animation
        if (weeklyTransactions.isEmpty()) {
            item {
                LoadingShimmer()
            }
        } else {
            itemsIndexed(weeklyTransactions) { index, day ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(
                        initialOffsetX = { it * (index + 1) },
                        animationSpec = androidx.compose.animation.core.tween(300, delayMillis = index * 50)
                    ) + fadeIn(animationSpec = androidx.compose.animation.core.tween(300, delayMillis = index * 50))
                ) {
                    ElevatedCard(
                        elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = day.date.format(dateFormatter), 
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                    Text(
                                        text = when (day.date.dayOfWeek) {
                                            java.time.DayOfWeek.MONDAY -> "Maanantai"
                                            java.time.DayOfWeek.TUESDAY -> "Tiistai"
                                            java.time.DayOfWeek.WEDNESDAY -> "Keskiviikko"
                                            java.time.DayOfWeek.THURSDAY -> "Torstai"
                                            java.time.DayOfWeek.FRIDAY -> "Perjantai"
                                            java.time.DayOfWeek.SATURDAY -> "Lauantai"
                                            java.time.DayOfWeek.SUNDAY -> "Sunnuntai"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "${if (day.netAmount >= 0) "+" else ""}€${formatter.format(day.netAmount)}", 
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = if (day.netAmount >= 0) IncomeGreen else ExpenseRed
                                )
                            }
                            
                            if (day.transactions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Show transaction details
                                day.transactions.forEach { transaction ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = transaction.note ?: "Tapahtuma",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${if (transaction.amount >= 0) "+" else ""}€${formatter.format(transaction.amount)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                                            color = if (transaction.amount >= 0) IncomeGreen else ExpenseRed
                                        )
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Ei tapahtumia",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}