package com.voitto.data.repository

import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.*
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataRepository @Inject constructor(
    private val budgetDao: BudgetDao
) {
    
    suspend fun seedSampleData() {
        // Check if data already exists
        val existingCategories = budgetDao.getAllCategories()
        if (existingCategories.isNotEmpty()) return
        
        // Seed categories
        val categories = listOf(
            CategoryEntity("food", "Ruoka", "🍽️", true),
            CategoryEntity("housing", "Asuminen", "🏠", true),
            CategoryEntity("transport", "Liikkuminen", "🚗", true),
            CategoryEntity("utilities", "Sähkö, vesi, jäte", "⚡", true),
            CategoryEntity("telecom", "Viestintä", "📱", true),
            CategoryEntity("health", "Terveys", "🏥", true),
            CategoryEntity("children", "Lapsiperhe", "👶", false),
            CategoryEntity("car", "Auto", "🚙", false),
            CategoryEntity("leisure", "Vapaa-aika", "🎮", false)
        )
        
        categories.forEach { budgetDao.insertCategory(it) }
        
        // Seed sample transactions (last 30 days)
        val today = LocalDate.now()
        val transactions = listOf(
            // Income
            TransactionEntity("income_1", today.minusDays(1), 2500f, "income", "Palkka", "manual"),
            TransactionEntity("income_2", today.minusDays(15), 2500f, "income", "Palkka", "manual"),
            TransactionEntity("income_3", today.minusDays(29), 2500f, "income", "Palkka", "manual"),
            
            // Housing expenses
            TransactionEntity("rent_1", today.minusDays(2), -650f, "housing", "Vuokra", "manual"),
            TransactionEntity("rent_2", today.minusDays(16), -650f, "housing", "Vuokra", "manual"),
            TransactionEntity("rent_3", today.minusDays(30), -650f, "housing", "Vuokra", "manual"),
            
            // Food expenses
            TransactionEntity("food_1", today.minusDays(1), -45f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_2", today.minusDays(3), -38f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_3", today.minusDays(5), -52f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_4", today.minusDays(7), -41f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_5", today.minusDays(10), -48f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_6", today.minusDays(12), -35f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_7", today.minusDays(14), -43f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_8", today.minusDays(17), -39f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_9", today.minusDays(20), -46f, "food", "Ruokakauppa", "manual"),
            TransactionEntity("food_10", today.minusDays(22), -44f, "food", "Ruokakauppa", "manual"),
            
            // Transport
            TransactionEntity("transport_1", today.minusDays(4), -25f, "transport", "Bussilippu", "manual"),
            TransactionEntity("transport_2", today.minusDays(8), -25f, "transport", "Bussilippu", "manual"),
            TransactionEntity("transport_3", today.minusDays(11), -25f, "transport", "Bussilippu", "manual"),
            TransactionEntity("transport_4", today.minusDays(15), -25f, "transport", "Bussilippu", "manual"),
            TransactionEntity("transport_5", today.minusDays(18), -25f, "transport", "Bussilippu", "manual"),
            TransactionEntity("transport_6", today.minusDays(21), -25f, "transport", "Bussilippu", "manual"),
            
            // Utilities
            TransactionEntity("electricity_1", today.minusDays(6), -85f, "utilities", "Sähkölasku", "manual"),
            TransactionEntity("water_1", today.minusDays(13), -45f, "utilities", "Vesilasku", "manual"),
            
            // Telecom
            TransactionEntity("phone_1", today.minusDays(9), -35f, "telecom", "Puhelinliittymä", "manual"),
            
            // Health
            TransactionEntity("pharmacy_1", today.minusDays(19), -28f, "health", "Apteekki", "manual"),
            
            // Children
            TransactionEntity("diapers_1", today.minusDays(23), -42f, "children", "Vaipat", "manual"),
            TransactionEntity("diapers_2", today.minusDays(26), -38f, "children", "Vaipat", "manual"),
            
            // Leisure
            TransactionEntity("netflix_1", today.minusDays(24), -12f, "leisure", "Netflix", "manual")
        )
        
        transactions.forEach { budgetDao.insertTransaction(it) }
        
        // Seed infrequent expenses
        val infrequentExpenses = listOf(
            ExpenseEntity(
                id = "vehicle_tax_2024",
                name = "Ajoneuvovero",
                categoryId = "car",
                amount = 200f,
                cadence = "annual",
                dueDate = today.plusDays(45),
                lastPaidAt = today.minusDays(320),
                confidenceScore = 0.9f,
                isInfrequent = true,
                notes = "Vuosittainen ajoneuvovero"
            ),
            ExpenseEntity(
                id = "inspection_2024",
                name = "Katsastus",
                categoryId = "car",
                amountRange = Pair(60f, 100f),
                cadence = "annual",
                dueDate = today.plusDays(30),
                lastPaidAt = today.minusDays(335),
                confidenceScore = 0.8f,
                isInfrequent = true,
                notes = "Auton vuosikatsastus"
            ),
            ExpenseEntity(
                id = "property_tax_2024",
                name = "Kiinteistövero",
                categoryId = "housing",
                amount = 450f,
                cadence = "annual",
                dueDate = today.plusDays(60),
                lastPaidAt = today.minusDays(305),
                confidenceScore = 0.95f,
                isInfrequent = true,
                notes = "Vuosittainen kiinteistövero"
            ),
            ExpenseEntity(
                id = "early_education_fee",
                name = "Varhaiskasvatusmaksu",
                categoryId = "children",
                amount = 120f,
                cadence = "monthly",
                dueDate = today.plusDays(5),
                lastPaidAt = today.minusDays(25),
                confidenceScore = 1.0f,
                isInfrequent = false,
                notes = "Kuukausittainen maksu"
            ),
            ExpenseEntity(
                id = "yle_tax_2024",
                name = "Yle-vero",
                categoryId = "utilities",
                amount = 0f,
                cadence = "annual",
                dueDate = today.plusDays(90),
                lastPaidAt = today.minusDays(275),
                confidenceScore = 1.0f,
                isInfrequent = true,
                notes = "Yleisradiovero (ilmainen)"
            )
        )
        
        infrequentExpenses.forEach { budgetDao.insertExpense(it) }
        
        // Seed current budget
        val currentBudget = BudgetEntity(
            id = "budget_${today.year}_${today.monthValue}",
            periodStart = today.withDayOfMonth(1),
            periodEnd = today.withDayOfMonth(today.lengthOfMonth()),
            categoryLimits = mapOf(
                "food" to 400f,
                "housing" to 700f,
                "transport" to 150f,
                "utilities" to 200f,
                "telecom" to 50f,
                "health" to 100f,
                "children" to 200f,
                "car" to 300f,
                "leisure" to 100f
            )
        )
        
        budgetDao.insertBudget(currentBudget)
    }
}
