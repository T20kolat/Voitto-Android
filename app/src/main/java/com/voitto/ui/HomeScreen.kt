package com.voitto.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitto.R
import com.voitto.ui.components.AnimatedBalance
import com.voitto.ui.components.AnimatedCard
import com.voitto.ui.components.LoadingShimmer
import com.voitto.ui.components.ReminderCard
import com.voitto.ui.components.ResponsiveContainer
import com.voitto.ui.components.ResponsiveCard
import com.voitto.ui.components.ResponsiveGrid
import com.voitto.ui.theme.getExpenseColor
import com.voitto.ui.theme.getIncomeColor
import com.voitto.ui.theme.getResponsivePadding
import com.voitto.ui.theme.getResponsiveSpacing
import com.voitto.ui.theme.getScreenSize
import com.voitto.ui.theme.getSuccessColor
import com.voitto.ui.theme.getResponsiveCardElevation
import com.voitto.ui.theme.getResponsiveIconSize
import com.voitto.ui.theme.getResponsiveButtonHeight
import com.voitto.ui.theme.getResponsiveCardPadding
import com.voitto.ui.theme.getResponsiveContentWidth
import com.voitto.ui.theme.getResponsiveGridColumns
import com.voitto.ui.theme.getResponsiveHorizontalArrangement
import com.voitto.ui.viewmodel.HomeViewModel
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, 
    contentPadding: PaddingValues = PaddingValues(getResponsivePadding()),
    onAddTransaction: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentBalance by viewModel.currentBalance.collectAsState()
    val safeToSpendResult by viewModel.safeToSpendResult.collectAsState()
    val upcomingExpenses by viewModel.upcomingExpenses.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val monthlyStats by viewModel.monthlyStats.collectAsState()
    val cashBurnInfo by viewModel.cashBurnInfo.collectAsState()
    val weeklyChallenge by viewModel.weeklyChallenge.collectAsState()
    
    val formatter = DecimalFormat("#,##0.00")
    
    var showChallengeDialog by remember { mutableStateOf(false) }
    var challengeAmount by remember { mutableStateOf("") }
    
    ResponsiveContainer(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(getResponsiveSpacing()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weekly Challenge - NOW THE TOP ITEM
            item {
                ResponsiveCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(getResponsiveIconSize())
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(id = R.string.challenge_of_week),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "UUSI!",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = weeklyChallenge.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = weeklyChallenge.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tavoite",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${formatter.format(weeklyChallenge.goal)}€",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Säästetty",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${formatter.format(weeklyChallenge.saved)}€",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = getSuccessColor()
                            )
                        }
                        
                        Button(
                            onClick = { 
                                // Show dialog to input savings amount
                                showChallengeDialog = true
                            },
                            modifier = Modifier.height(getResponsiveButtonHeight()),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (weeklyChallenge.isCompleted) 
                                    getSuccessColor()
                                else 
                                    MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = if (weeklyChallenge.isCompleted) "✅ Valmis!" else "Merkitse",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            
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
                    amount = safeToSpendResult?.let { 
                        val safeAmount = if (it.safeToSpend > 0) it.safeToSpend else 0f
                        "€${formatter.format(safeAmount)}"
                    } ?: "€0,00",
                    isPositive = (safeToSpendResult?.safeToSpend ?: 0f) >= 0
                )
            }

            // Monthly Statistics
            item {
                ResponsiveCard {
                    Text(
                        text = "Tämän kuun tilanne",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = getResponsiveHorizontalArrangement()
                    ) {
                        Column {
                            Text(
                                text = "Tulot",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = null,
                                    tint = getIncomeColor(),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "€${formatter.format(monthlyStats.income)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = getIncomeColor()
                                )
                            }
                        }
                        
                        Column {
                            Text(
                                text = "Menot",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = getExpenseColor(),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "€${formatter.format(monthlyStats.expenses)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = getExpenseColor()
                                )
                            }
                        }
                        
                        Column {
                            Text(
                                text = "Säästö",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "€${formatter.format(monthlyStats.savings)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = if (monthlyStats.savings >= 0) 
                                    getSuccessColor() 
                                else 
                                    getExpenseColor()
                            )
                        }
                    }
                }
            }
            
            // Cash Burn Rate
            item {
                ResponsiveCard {
                    Text(
                        text = "Kassan kulutus",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = getResponsiveHorizontalArrangement()
                    ) {
                        Column {
                            Text(
                                text = "Päivittäinen kulutus",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "€${formatter.format(cashBurnInfo.dailyBurnRate)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = androidx.compose.ui.graphics.Color(0xFFF44336)
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Päiviä palkkaan",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${cashBurnInfo.daysUntilNextSalary} pv",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Jäljellä palkkaan",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "€${formatter.format(cashBurnInfo.moneyLeftUntilSalary)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = if (cashBurnInfo.moneyLeftUntilSalary >= 0) 
                                    androidx.compose.ui.graphics.Color(0xFF4CAF50) 
                                else 
                                    androidx.compose.ui.graphics.Color(0xFFF44336)
                            )
                        }
                    }
                }
            }

            // Recent Transactions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Viimeisimmät tapahtumat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Button(
                        onClick = onAddTransaction,
                        modifier = Modifier.height(getResponsiveButtonHeight())
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(getResponsiveIconSize() * 0.5f))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lisää", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            
            items(recentTransactions.take(5)) { transaction ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = getResponsiveCardElevation())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(getResponsiveCardPadding()),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = transaction.note ?: "Tapahtuma",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                            Text(
                                text = transaction.date.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM")),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${if (transaction.amount >= 0) "+" else ""}€${formatter.format(transaction.amount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = if (transaction.amount >= 0) 
                                androidx.compose.ui.graphics.Color(0xFF4CAF50) 
                            else 
                                androidx.compose.ui.graphics.Color(0xFFF44336)
                        )
                    }
                }
            }

            // Show upcoming expenses with staggered animation
            itemsIndexed(upcomingExpenses) { index, expense ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it * (index + 1) },
                        animationSpec = androidx.compose.animation.core.tween(300, delayMillis = index * 100)
                    ) + fadeIn(animationSpec = androidx.compose.animation.core.tween(300, delayMillis = index * 100))
                ) {
                    val amountText = expense.amount?.let { "€${formatter.format(it)}" } 
                        ?: expense.amountRange?.let { "€${formatter.format(it.first)}–${formatter.format(it.second)}" }
                        ?: "€?"
                        
                    ReminderCard(
                        title = stringResource(id = R.string.predicted_expense_title),
                        subtitle = "${expense.name} — $amountText",
                        onConfirm = { viewModel.confirmExpense(expense) },
                        onSnooze = { viewModel.snoozeExpense(expense) },
                        onDismiss = { viewModel.dismissExpense(expense) }
                    )
                }
            }
        }
    }
    
    // Challenge Progress Dialog
    if (showChallengeDialog) {
        AlertDialog(
            onDismissRequest = { 
                showChallengeDialog = false
                challengeAmount = ""
            },
            title = {
                Text("Merkitse säästö")
            },
            text = {
                Column {
                    Text("Kuinka paljon säästit tällä kertaa?")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = challengeAmount,
                        onValueChange = { challengeAmount = it },
                        label = { Text("Säästösumma (€)") },
                        placeholder = { Text("0.00") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amount = challengeAmount.toFloatOrNull() ?: 0f
                        if (amount > 0f) {
                            viewModel.markChallengeProgress(amount)
                        }
                        showChallengeDialog = false
                        challengeAmount = ""
                    }
                ) {
                    Text("Merkitse")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showChallengeDialog = false
                        challengeAmount = ""
                    }
                ) {
                    Text("Peruuta")
                }
            }
        )
    }
}