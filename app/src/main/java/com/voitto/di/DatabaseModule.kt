package com.voitto.di

import android.content.Context
import com.voitto.data.dao.BudgetDao
import com.voitto.data.database.VoittoDatabase
import com.voitto.data.repository.ResourceRepository
import com.voitto.data.repository.SampleDataRepository
import com.voitto.domain.usecase.SafeToSpendUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideVoittoDatabase(@ApplicationContext context: Context): VoittoDatabase {
        return VoittoDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideBudgetDao(database: VoittoDatabase): BudgetDao {
        return database.budgetDao()
    }
    
    @Provides
    @Singleton
    fun provideResourceRepository(budgetDao: BudgetDao): ResourceRepository {
        return ResourceRepository(budgetDao)
    }
    
    @Provides
    @Singleton
    fun provideSafeToSpendUseCase(budgetDao: BudgetDao): SafeToSpendUseCase {
        return SafeToSpendUseCase(budgetDao)
    }
    
    @Provides
    @Singleton
    fun provideSampleDataRepository(budgetDao: BudgetDao): SampleDataRepository {
        return SampleDataRepository(budgetDao)
    }
}
