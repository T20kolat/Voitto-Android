package com.voitto.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.*

@Database(
    entities = [
        BudgetEntity::class,
        CategoryEntity::class,
        ExpenseEntity::class,
        TransactionEntity::class,
        SinkingFundEntity::class,
        ResourceEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VoittoDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    
    companion object {
        @Volatile
        private var INSTANCE: VoittoDatabase? = null
        
        fun getDatabase(context: Context): VoittoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VoittoDatabase::class.java,
                    "voitto_database"
                )
                .fallbackToDestructiveMigration() // For development - in production use proper migrations
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
