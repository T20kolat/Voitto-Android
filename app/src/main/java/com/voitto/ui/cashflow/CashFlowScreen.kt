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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.voitto.ui.components.AnimatedBalance
import com.voitto.ui.components.AnimatedButton
import com.voitto.ui.components.AnimatedTransactionBar
import com.voitto.ui.components.LoadingShimmer
import com.voitto.ui.theme.IncomeGreen
import com.voitto.ui.theme.ExpenseRed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitto.R
import com.voitto.ui.viewmodel.CashFlowViewModel
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@Composable
fun CashFlowScreen(
    modifier: Modifier = Modifier,
    viewModel: CashFlowViewModel = hiltViewModel()
) {
    val currentBalance by viewModel.currentBalance.collectAsState()
    val safeToSpendResult by viewModel.safeToSpendResult.collectAsState()
    val selectedWeek by viewModel.selectedWeek.collectAsState()
    val weeklyTransactions by viewModel.weeklyTransactions.collectAsState()
    
    val formatter = DecimalFormat("#,##0.00")
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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

        // Week navigation
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedButton(onClick = { viewModel.selectPreviousWeek() }) {
                    Text("←")
                }
                Text(
                    text = "${selectedWeek.format(dateFormatter)} - ${selectedWeek.plusDays(6).format(dateFormatter)}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
                AnimatedButton(onClick = { viewModel.selectNextWeek() }) {
                    Text("→")
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
                        Row(modifier = Modifier.padding(16.dp)) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = day.date.format(dateFormatter), 
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                if (day.transactions.isNotEmpty()) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        day.transactions.forEach { transaction ->
                                            AnimatedTransactionBar(
                                                amount = transaction.amount, 
                                                color = if (transaction.amount >= 0) IncomeGreen else ExpenseRed
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "${if (day.netAmount >= 0) "+" else ""}€${formatter.format(day.netAmount)}", 
                                style = MaterialTheme.typography.titleSmall,
                                color = if (day.netAmount >= 0) IncomeGreen else ExpenseRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CashFlowHeader(
    balanceLabel: String,
    safeToSpendLabel: String,
    balanceAmount: String,
    stsAmount: String
) {
    ElevatedCard {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = balanceLabel, style = MaterialTheme.typography.labelLarge)
            Text(text = balanceAmount, style = MaterialTheme.typography.headlineSmall)
            Text(text = safeToSpendLabel, style = MaterialTheme.typography.labelLarge)
            Text(text = stsAmount, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun CashFlowPreviewStrip() {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(30f, -20f, 10f, -15f, 25f).forEach { v ->
            AnimatedTransactionBar(
                amount = v, 
                color = if (v >= 0f) IncomeGreen else ExpenseRed
            )
        }
    }
}

@Composable
private fun Bar(amount: Float, color: Color) {
    val heightDp = (kotlin.math.abs(amount) / 2).dp.coerceAtLeast(8.dp)
    Spacer(
        modifier = Modifier
            .height(heightDp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
    )
}

