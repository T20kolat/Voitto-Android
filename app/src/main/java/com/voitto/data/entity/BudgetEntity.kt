package com.voitto.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val categoryLimits: Map<String, Float> // category -> limit in EUR
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val isEssential: Boolean
)

@Entity(
    tableName = "expenses",
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["dueDate"]),
        Index(value = ["isInfrequent"]),
        Index(value = ["confidenceScore"])
    ]
)
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val categoryId: String,
    val amount: Float? = null, // null for variable amounts
    val amountRange: Pair<Float, Float>? = null, // min, max for ranges
    val cadence: String, // "monthly", "quarterly", "annual", "variable"
    val dueDate: LocalDate? = null,
    val lastPaidAt: LocalDate? = null,
    val confidenceScore: Float = 0.5f, // 0.0 to 1.0
    val isInfrequent: Boolean = false,
    val notes: String? = null
)

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["date"]),
        Index(value = ["categoryId"]),
        Index(value = ["source"])
    ]
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val date: LocalDate,
    val amount: Float, // positive for income, negative for expenses
    val categoryId: String,
    val note: String? = null,
    val source: String = "manual" // "manual", "import", "predicted"
)

@Entity(tableName = "sinking_funds")
data class SinkingFundEntity(
    @PrimaryKey val id: String,
    val name: String,
    val targetAmount: Float,
    val monthlySetAside: Float,
    val currentBalance: Float = 0f
)

@Entity(
    tableName = "resources",
    indices = [
        Index(value = ["type"]),
        Index(value = ["lastVerified"])
    ]
)
data class ResourceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // "kela", "ruoka_apu", "velkaneuvonta", "tyo_koulutus", "asuminen"
    val eligibilitySummary: String,
    val requiredDocuments: List<String>,
    val applicationLink: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val hours: String? = null,
    val languages: List<String> = listOf("fi"),
    val regionTags: List<String> = listOf("finland"),
    val lastVerified: LocalDate? = null
)
