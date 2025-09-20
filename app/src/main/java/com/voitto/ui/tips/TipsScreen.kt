package com.voitto.ui.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voitto.R

data class SavingTip(
    val title: String,
    val description: String,
    val estimatedSaving: String,
    val difficulty: String,
    val category: String
)

@Composable
fun TipsScreen(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(16.dp)) {
    val tips = listOf(
        SavingTip(
            title = "Vaihda brändituotteet kaupan merkkiin",
            description = "Kokeile kaupan omia merkkejä ruokakaupassa. Laatu on usein yhtä hyvä, mutta hinta paljon halvempi.",
            estimatedSaving = "4-8 €/viikko",
            difficulty = "Helppo",
            category = "Ruoka"
        ),
        SavingTip(
            title = "Käytä julkista liikennettä",
            description = "Jos mahdollista, käytä bussia tai junaa auton sijaan. Säästät polttoainekustannuksia ja parkkimaksuja.",
            estimatedSaving = "20-50 €/viikko",
            difficulty = "Keskitaso",
            category = "Liikenne"
        ),
        SavingTip(
            title = "Sammuta valot ja laitteet",
            description = "Muista sammuttaa valot ja irrottaa laitteet pistorasiasta, kun et käytä niitä.",
            estimatedSaving = "5-15 €/kuukausi",
            difficulty = "Helppo",
            category = "Sähkö"
        ),
        SavingTip(
            title = "Käytä ilmaisia aktiviteetteja",
            description = "Käy luonnossa, kirjastossa tai ilmaisissa tapahtumissa vapaa-ajan vieton sijaan.",
            estimatedSaving = "30-100 €/kuukausi",
            difficulty = "Helppo",
            category = "Vapaa-aika"
        ),
        SavingTip(
            title = "Vertaa vakuutushintoja",
            description = "Tarkista vakuutushintasi vuosittain ja vertaile tarjouksia eri vakuutusyhtiöiltä.",
            estimatedSaving = "50-200 €/vuosi",
            difficulty = "Keskitaso",
            category = "Vakuutukset"
        )
    )
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.tips_title), 
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(tips) { tip ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tip.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = tip.estimatedSaving,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = tip.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tip.difficulty,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Text(
                            text = tip.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

