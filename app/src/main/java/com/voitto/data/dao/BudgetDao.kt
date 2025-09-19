package com.voitto.data.dao

import androidx.room.*
import com.voitto.data.entity.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface BudgetDao {
    
    @Query("SELECT * FROM budgets WHERE periodStart <= :date AND periodEnd >= :date LIMIT 1")
    suspend fun getCurrentBudget(date: LocalDate = LocalDate.now()): BudgetEntity?
    
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsInPeriod(startDate: LocalDate, endDate: LocalDate): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM expenses WHERE isInfrequent = 1 AND (dueDate IS NULL OR dueDate BETWEEN :startDate AND :endDate)")
    suspend fun getUpcomingInfrequentExpenses(startDate: LocalDate, endDate: LocalDate): List<ExpenseEntity>
    
    @Query("SELECT * FROM expenses WHERE confidenceScore >= :minConfidence ORDER BY confidenceScore DESC")
    suspend fun getPredictedExpenses(minConfidence: Float = 0.6f): List<ExpenseEntity>
    
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>
    
    @Query("SELECT * FROM resources WHERE type = :type")
    suspend fun getResourcesByType(type: String): List<ResourceEntity>
    
    @Query("SELECT * FROM resources WHERE regionTags LIKE '%' || :region || '%'")
    suspend fun getResourcesByRegion(region: String): List<ResourceEntity>
    
    // Optimized query with pagination
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getTransactionsPaginated(startDate: LocalDate, endDate: LocalDate, limit: Int, offset: Int): List<TransactionEntity>
    
    // Cached query for frequently accessed data
    @Query("SELECT COUNT(*) FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTransactionCount(startDate: LocalDate, endDate: LocalDate): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: ResourceEntity)
    
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
    
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
}
