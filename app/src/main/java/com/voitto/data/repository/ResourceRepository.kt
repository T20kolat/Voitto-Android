package com.voitto.data.repository

import com.voitto.data.dao.BudgetDao
import com.voitto.data.entity.ResourceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResourceRepository(private val budgetDao: BudgetDao) {
    
    fun getResourcesByType(type: String): Flow<List<ResourceEntity>> = flow {
        emit(budgetDao.getResourcesByType(type))
    }
    
    fun getResourcesByRegion(region: String): Flow<List<ResourceEntity>> = flow {
        emit(budgetDao.getResourcesByRegion(region))
    }
    
    suspend fun insertResource(resource: ResourceEntity) {
        budgetDao.insertResource(resource)
    }
    
    // Seed initial Finnish resources
    suspend fun seedInitialResources() {
        val initialResources = listOf(
            ResourceEntity(
                id = "kela_toimeentulotuki",
                name = "Toimeentulotuki",
                type = "kela",
                eligibilitySummary = "Jos tulot ja varat eivät riitä elämiseen",
                requiredDocuments = listOf("Henkilöllisyystodistus", "Tuloerittely", "Vuokrasopimus"),
                applicationLink = "https://www.kela.fi/toimeentulotuki",
                phone = "020 634 0000",
                lastVerified = java.time.LocalDate.now()
            ),
            ResourceEntity(
                id = "kela_asumistuki",
                name = "Yleinen asumistuki",
                type = "kela",
                eligibilitySummary = "Asumiskustannusten tuki",
                requiredDocuments = listOf("Vuokrasopimus", "Tuloerittely", "Henkilöllisyystodistus"),
                applicationLink = "https://www.kela.fi/asumistuki",
                phone = "020 634 0000",
                lastVerified = java.time.LocalDate.now()
            ),
            ResourceEntity(
                id = "ruoka_apu_helsinki",
                name = "Helsingin ruoka-apu",
                type = "ruoka_apu",
                eligibilitySummary = "Ruokajako Helsingissä",
                requiredDocuments = listOf("Henkilöllisyystodistus", "Tuloerittely"),
                phone = "09 310 12345",
                address = "Helsinki",
                lastVerified = java.time.LocalDate.now()
            ),
            ResourceEntity(
                id = "velkaneuvonta_takuusaatio",
                name = "Takuusäätiö - Velkaneuvonta",
                type = "velkaneuvonta",
                eligibilitySummary = "Ilmainen velkaneuvonta ja takaukset",
                requiredDocuments = listOf("Henkilöllisyystodistus", "Velkaluettelo"),
                applicationLink = "https://www.takuusaatio.fi",
                phone = "020 123 4567",
                lastVerified = java.time.LocalDate.now()
            ),
            ResourceEntity(
                id = "te_palvelut",
                name = "TE-palvelut",
                type = "tyo_koulutus",
                eligibilitySummary = "Työnhaku ja työttömyysturva",
                requiredDocuments = listOf("Henkilöllisyystodistus", "Työtodistukset"),
                applicationLink = "https://www.te-palvelut.fi",
                phone = "0295 020 800",
                lastVerified = java.time.LocalDate.now()
            )
        )
        
        initialResources.forEach { resource ->
            budgetDao.insertResource(resource)
        }
    }
}
