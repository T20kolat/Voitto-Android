package com.voitto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.ExpenseEntity
import com.voitto.data.repository.SampleDataRepository
import com.voitto.domain.usecase.SafeToSpendUseCase
import com.voitto.domain.usecase.SafeToSpendResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val budgetDao: BudgetDao,
    private val safeToSpendUseCase: SafeToSpendUseCase,
    private val sampleDataRepository: SampleDataRepository
) : ViewModel() {
    
    private val _currentBalance = MutableStateFlow(1000f) // TODO: Get from user settings
    val currentBalance: StateFlow<Float> = _currentBalance.asStateFlow()
    
    val safeToSpendResult: StateFlow<SafeToSpendResult?> = 
        _currentBalance.flatMapLatest { balance ->
            safeToSpendUseCase.calculateSafeToSpend(balance)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    private val _upcomingExpenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val upcomingExpenses: StateFlow<List<ExpenseEntity>> = _upcomingExpenses.asStateFlow()
    
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
}
