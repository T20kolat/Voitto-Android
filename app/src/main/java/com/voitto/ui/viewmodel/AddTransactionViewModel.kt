package com.voitto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.CategoryEntity
import com.voitto.data.entity.TransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val budgetDao: BudgetDao
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val categories: StateFlow<List<CategoryEntity>> = flow {
        _isLoading.value = true
        try {
            emit(budgetDao.getAllCategories())
        } finally {
            _isLoading.value = false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            try {
                budgetDao.insertTransaction(transaction)
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}
