package com.voitto.domain.usecase

import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.ExpenseEntity
import com.voitto.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class SafeToSpendResult(
    val currentBalance: Float,
    val safeToSpend: Float,
    val upcomingExpenses: List<UpcomingExpense>,
    val explanation: String
)

data class UpcomingExpense(
    val name: String,
    val amount: Float,
    val dueDate: LocalDate,
    val isPredicted: Boolean
)

class SafeToSpendUseCase(private val budgetDao: BudgetDao) {
    
    fun calculateSafeToSpend(
        currentBalance: Float,
        daysAhead: Int = 7
    ): Flow<SafeToSpendResult> {
        val endDate = LocalDate.now().plusDays(daysAhead.toLong())
        val startDate = LocalDate.now()
        
        return combine(
            budgetDao.getTransactionsInPeriod(startDate, endDate),
            budgetDao.getUpcomingInfrequentExpenses(startDate, endDate)
        ) { transactions, expenses ->
            calculateSafeToSpendInternal(
                currentBalance = currentBalance,
                transactions = transactions,
                expenses = expenses,
                daysAhead = daysAhead
            )
        }
    }
    
    private fun calculateSafeToSpendInternal(
        currentBalance: Float,
        transactions: List<TransactionEntity>,
        expenses: List<ExpenseEntity>,
        daysAhead: Int
    ): SafeToSpendResult {
        // Calculate net from transactions in the period
        val netFromTransactions = transactions.sumOf { it.amount.toDouble() }.toFloat()
        
        // Calculate upcoming expenses (both confirmed and predicted)
        val upcomingExpenses = expenses.map { expense ->
            val amount = expense.amount ?: (expense.amountRange?.first ?: 0f)
            UpcomingExpense(
                name = expense.name,
                amount = amount,
                dueDate = expense.dueDate ?: LocalDate.now().plusDays(1),
                isPredicted = expense.confidenceScore < 0.8f
            )
        }
        
        val totalUpcomingExpenses = upcomingExpenses.sumOf { it.amount.toDouble() }.toFloat()
        
        // Safe to spend = current balance + net transactions - upcoming expenses
        val safeToSpend = currentBalance + netFromTransactions - totalUpcomingExpenses
        
        // Create explanation
        val explanation = buildString {
            append("Turvallisesti käytettävissä on arvio rahasta, jonka voit käyttää seuraavan ${daysAhead} päivän aikana. ")
            append("Siinä on huomioitu tulevat laskut, kuten ")
            if (upcomingExpenses.isNotEmpty()) {
                append(upcomingExpenses.take(2).joinToString(" ja ") { it.name })
                if (upcomingExpenses.size > 2) append(" ja ${upcomingExpenses.size - 2} muuta")
            } else {
                append("ei tulevia laskuja")
            }
            append(".")
        }
        
        return SafeToSpendResult(
            currentBalance = currentBalance,
            safeToSpend = safeToSpend,
            upcomingExpenses = upcomingExpenses,
            explanation = explanation
        )
    }
}
