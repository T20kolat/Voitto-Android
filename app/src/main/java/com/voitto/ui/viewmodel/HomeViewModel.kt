package com.voitto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.ExpenseEntity
import com.voitto.data.entity.TransactionEntity
import com.voitto.data.repository.SampleDataRepository
import com.voitto.domain.usecase.SafeToSpendUseCase
import com.voitto.domain.usecase.SafeToSpendResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class MonthlyStats(
    val income: Float,
    val expenses: Float,
    val savings: Float
)

data class CashBurnInfo(
    val dailyBurnRate: Float,
    val daysUntilNextSalary: Int,
    val moneyLeftUntilSalary: Float
)

data class WeeklyChallenge(
    val title: String,
    val description: String,
    val goal: Float,
    val saved: Float,
    val isCompleted: Boolean
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val budgetDao: BudgetDao,
    private val safeToSpendUseCase: SafeToSpendUseCase,
    private val sampleDataRepository: SampleDataRepository
) : ViewModel() {
    
    val currentBalance: StateFlow<Float> = budgetDao
        .getTransactionsInPeriod(
            LocalDate.now().withDayOfMonth(1), // Start of current month
            LocalDate.now()
        ).map { transactions ->
            // Calculate current balance from current month transactions only
            transactions.sumOf { it.amount.toDouble() }.toFloat()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0f
        )
    
    val safeToSpendResult: StateFlow<SafeToSpendResult?> = 
        currentBalance.flatMapLatest { balance ->
            // Calculate a more realistic safe to spend amount
            budgetDao.getTransactionsInPeriod(
                LocalDate.now().minusDays(30),
                LocalDate.now()
            ).map { transactions ->
                val dailyExpenses = transactions.filter { it.amount < 0 }
                    .sumOf { kotlin.math.abs(it.amount.toDouble()) }.toFloat() / 30f
                
                // Safe to spend = current balance - (daily expenses * 7 days) - buffer
                val weeklyExpenses = dailyExpenses * 7f
                val buffer = 200f // 200€ buffer for unexpected expenses
                val safeAmount = (balance - weeklyExpenses - buffer).coerceAtLeast(0f)
                
                SafeToSpendResult(
                    currentBalance = balance,
                    safeToSpend = safeAmount,
                    upcomingExpenses = emptyList(),
                    explanation = "Turvallisesti käytettävissä on rahaa, josta on vähennetty viikon arvioitu kulutus ja 200€ puskuri."
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    private val _upcomingExpenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val upcomingExpenses: StateFlow<List<ExpenseEntity>> = _upcomingExpenses.asStateFlow()
    
    private val _weeklyChallenge = MutableStateFlow(
        WeeklyChallenge(
            title = "Soodaton viikko — merkitse suoritukset",
            description = "Säästä tällä viikolla vähintään 50€ vaihtamalla brändituotteet kaupan omiin merkkeihin. Merkitse jokainen säästö!",
            goal = 50f,
            saved = 0f,
            isCompleted = false
        )
    )
    val weeklyChallenge: StateFlow<WeeklyChallenge> = _weeklyChallenge.asStateFlow()
    
    val recentTransactions: StateFlow<List<TransactionEntity>> = budgetDao
        .getTransactionsInPeriod(
            LocalDate.now().minusDays(30),
            LocalDate.now()
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val monthlyStats: StateFlow<MonthlyStats> = budgetDao
        .getTransactionsInPeriod(
            LocalDate.now().withDayOfMonth(1),
            LocalDate.now()
        ).map { transactions ->
            val income = transactions.filter { it.amount > 0 }.sumOf { it.amount.toDouble() }.toFloat()
            val expenses = transactions.filter { it.amount < 0 }.sumOf { kotlin.math.abs(it.amount.toDouble()) }.toFloat()
            MonthlyStats(
                income = income,
                expenses = expenses,
                savings = income - expenses
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MonthlyStats(0f, 0f, 0f)
        )
    
    val cashBurnInfo: StateFlow<CashBurnInfo> = budgetDao
        .getTransactionsInPeriod(
            LocalDate.now().minusDays(30),
            LocalDate.now()
        ).map { transactions ->
            val dailyExpenses = transactions.filter { it.amount < 0 }
                .sumOf { kotlin.math.abs(it.amount.toDouble()) }.toFloat() / 30f
            
            // Assume next salary is on 15th of next month
            val today = LocalDate.now()
            val nextSalary = if (today.dayOfMonth < 15) {
                today.withDayOfMonth(15)
            } else {
                today.plusMonths(1).withDayOfMonth(15)
            }
            val daysUntilSalary = java.time.temporal.ChronoUnit.DAYS.between(today, nextSalary).toInt()
            
            val currentBalance = transactions.sumOf { it.amount.toDouble() }.toFloat()
            val moneyLeftUntilSalary = currentBalance - (dailyExpenses * daysUntilSalary)
            
            CashBurnInfo(
                dailyBurnRate = dailyExpenses,
                daysUntilNextSalary = daysUntilSalary,
                moneyLeftUntilSalary = moneyLeftUntilSalary
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CashBurnInfo(0f, 0, 0f)
        )
    
    init {
        // Load upcoming expenses immediately for better UX
        loadUpcomingExpenses()
        // Initialize sample data in background
        initializeSampleData()
    }
    
    private fun initializeSampleData() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                sampleDataRepository.seedSampleData()
                // Reload expenses after sample data is seeded
                loadUpcomingExpenses()
            } catch (e: Exception) {
                // Handle error gracefully - sample data is not critical
                android.util.Log.w("HomeViewModel", "Failed to seed sample data", e)
            }
        }
    }
    
    private fun loadUpcomingExpenses() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val nextWeek = today.plusDays(7)
            val expenses = budgetDao.getUpcomingInfrequentExpenses(today, nextWeek)
            _upcomingExpenses.value = expenses
        }
    }
    
    fun confirmExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            val updatedExpense = expense.copy(
                confidenceScore = 1.0f,
                dueDate = expense.dueDate ?: LocalDate.now().plusDays(30)
            )
            budgetDao.updateExpense(updatedExpense)
            loadUpcomingExpenses()
        }
    }
    
    fun snoozeExpense(expense: ExpenseEntity, days: Int = 7) {
        viewModelScope.launch {
            val updatedExpense = expense.copy(
                dueDate = (expense.dueDate ?: LocalDate.now()).plusDays(days.toLong())
            )
            budgetDao.updateExpense(updatedExpense)
            loadUpcomingExpenses()
        }
    }
    
    fun dismissExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            budgetDao.deleteExpense(expense)
            loadUpcomingExpenses()
        }
    }
    
    fun markChallengeProgress(amount: Float) {
        val currentChallenge = _weeklyChallenge.value
        val newSaved = (currentChallenge.saved + amount).coerceAtMost(currentChallenge.goal)
        val isCompleted = newSaved >= currentChallenge.goal
        
        _weeklyChallenge.value = currentChallenge.copy(
            saved = newSaved,
            isCompleted = isCompleted
        )
    }
}
