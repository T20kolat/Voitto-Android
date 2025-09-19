package com.voitto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.TransactionEntity
import com.voitto.domain.usecase.SafeToSpendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class CashFlowDay(
    val date: LocalDate,
    val transactions: List<TransactionEntity>,
    val netAmount: Float
)

@HiltViewModel
class CashFlowViewModel @Inject constructor(
    private val budgetDao: BudgetDao,
    private val safeToSpendUseCase: SafeToSpendUseCase
) : ViewModel() {
    
    private val _currentBalance = MutableStateFlow(1000f)
    val currentBalance: StateFlow<Float> = _currentBalance.asStateFlow()
    
    val safeToSpendResult = _currentBalance.flatMapLatest { balance ->
        safeToSpendUseCase.calculateSafeToSpend(balance)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    private val _selectedWeek = MutableStateFlow(LocalDate.now())
    val selectedWeek: StateFlow<LocalDate> = _selectedWeek.asStateFlow()
    
    val weeklyTransactions: StateFlow<List<CashFlowDay>> = _selectedWeek.flatMapLatest { weekStart ->
        val weekEnd = weekStart.plusDays(6)
        budgetDao.getTransactionsInPeriod(weekStart, weekEnd).map { transactions ->
            // Optimize grouping with pre-allocated map
            val transactionsByDay = transactions.groupByTo(
                destination = mutableMapOf(),
                keySelector = { it.date }
            )
            
            // Create CashFlowDay for each day in the week (optimized)
            (0..6).map { dayOffset ->
                val date = weekStart.plusDays(dayOffset.toLong())
                val dayTransactions = transactionsByDay[date] ?: emptyList()
                // Use sumOf with toDouble() for better performance
                val netAmount = dayTransactions.sumOf { it.amount.toDouble() }.toFloat()
                
                CashFlowDay(
                    date = date,
                    transactions = dayTransactions,
                    netAmount = netAmount
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    fun selectPreviousWeek() {
        _selectedWeek.value = _selectedWeek.value.minusWeeks(1)
    }
    
    fun selectNextWeek() {
        _selectedWeek.value = _selectedWeek.value.plusWeeks(1)
    }
    
    fun selectCurrentWeek() {
        _selectedWeek.value = LocalDate.now()
    }
}
