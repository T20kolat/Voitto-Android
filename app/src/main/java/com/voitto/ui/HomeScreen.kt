package com.voitto.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitto.R
import com.voitto.ui.components.AnimatedBalance
import com.voitto.ui.components.AnimatedCard
import com.voitto.ui.components.LoadingShimmer
import com.voitto.ui.components.ReminderCard
import com.voitto.ui.cashflow.CashFlowPreviewStrip
import com.voitto.ui.viewmodel.HomeViewModel
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, 
    contentPadding: PaddingValues = PaddingValues(16.dp),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentBalance by viewModel.currentBalance.collectAsState()
    val safeToSpendResult by viewModel.safeToSpendResult.collectAsState()
    val upcomingExpenses by viewModel.upcomingExpenses.collectAsState()
    
    val formatter = DecimalFormat("#,##0.00")
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

        item {
            AnimatedCard {
                CashFlowPreviewStrip()
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

        item {
            AnimatedCard {
                Text(
                    text = stringResource(id = R.string.challenge_of_week),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Soodaton viikko — merkitse suoritukset",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

