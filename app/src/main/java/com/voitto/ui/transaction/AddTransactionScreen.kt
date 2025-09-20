package com.voitto.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitto.R
import com.voitto.data.entity.CategoryEntity
import com.voitto.data.entity.TransactionEntity
import com.voitto.ui.viewmodel.AddTransactionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isIncome by remember { mutableStateOf(false) }
    
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = stringResource(id = R.string.add_transaction),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
                // Transaction Type Toggle
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.transaction_type),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = !isIncome,
                                    onClick = { isIncome = false },
                                    label = { Text(stringResource(id = R.string.expense)) },
                                    modifier = Modifier.weight(1f)
                                )
                                FilterChip(
                                    selected = isIncome,
                                    onClick = { isIncome = true },
                                    label = { Text(stringResource(id = R.string.income)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                
                // Amount Input
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.amount),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = amount,
                                onValueChange = { amount = it },
                                label = { Text("€") },
                                placeholder = { Text("0.00") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                                )
                            )
                        }
                    }
                }
                
                // Category Selection
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.category),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.height(200.dp)
                                ) {
                                    items(categories) { category ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { selectedCategory = category },
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = if (selectedCategory?.id == category.id) 4.dp else 2.dp
                                            ),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (selectedCategory?.id == category.id) 
                                                    MaterialTheme.colorScheme.primaryContainer 
                                                else 
                                                    MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = category.icon,
                                                    style = MaterialTheme.typography.titleLarge,
                                                    modifier = Modifier.padding(end = 12.dp)
                                                )
                                                Text(
                                                    text = category.name,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = if (selectedCategory?.id == category.id) 
                                                        FontWeight.Bold 
                                                    else 
                                                        FontWeight.Normal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Date Selection
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.date),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = selectedDate.format(formatter),
                                onValueChange = { /* TODO: Implement date picker */ },
                                label = { Text(stringResource(id = R.string.date)) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                        }
                    }
                }
                
                // Note Input
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.note),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = note,
                                onValueChange = { note = it },
                                label = { Text(stringResource(id = R.string.note_optional)) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 3
                            )
                        }
                    }
                }
                
                
                // Submit Button - ALIGNED WITH OTHER CARDS
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCategory != null && amount.isNotEmpty() && amount.toFloatOrNull() != null) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Button(
                            onClick = {
                                val transactionAmount = amount.toFloatOrNull() ?: 0f
                                val finalAmount = if (isIncome) transactionAmount else -transactionAmount
                                
                                if (selectedCategory != null && finalAmount != 0f) {
                                    val transaction = TransactionEntity(
                                        id = "manual_${System.currentTimeMillis()}",
                                        date = selectedDate,
                                        amount = finalAmount,
                                        categoryId = selectedCategory!!.id,
                                        note = note.ifEmpty { null },
                                        source = "manual"
                                    )
                                    viewModel.addTransaction(transaction)
                                    onNavigateBack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selectedCategory != null && amount.isNotEmpty() && amount.toFloatOrNull() != null,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategory != null && amount.isNotEmpty() && amount.toFloatOrNull() != null) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = if (selectedCategory != null && amount.isNotEmpty() && amount.toFloatOrNull() != null) 
                                    "LISÄÄ TAPAHTUMA" 
                                else 
                                    "TÄYTÄ KAIKKI KENTÄT",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedCategory != null && amount.isNotEmpty() && amount.toFloatOrNull() != null) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Add bottom padding for navigation bar
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }