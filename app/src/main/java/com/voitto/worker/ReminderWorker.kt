package com.voitto.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.ExpenseEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val budgetDao: BudgetDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val upcomingExpenses = getUpcomingExpenses()
            if (upcomingExpenses.isNotEmpty()) {
                // TODO: Send notifications for upcoming expenses
                // For now, just log them
                upcomingExpenses.forEach { expense ->
                    android.util.Log.d("ReminderWorker", "Upcoming expense: ${expense.name} due ${expense.dueDate}")
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    private suspend fun getUpcomingExpenses(): List<ExpenseEntity> {
        val today = LocalDate.now()
        val nextWeek = today.plusDays(7)
        
        return budgetDao.getUpcomingInfrequentExpenses(today, nextWeek)
    }
}
