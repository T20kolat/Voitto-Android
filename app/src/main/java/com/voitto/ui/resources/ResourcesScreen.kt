package com.voitto.ui.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitto.R
import com.voitto.ui.viewmodel.ResourcesViewModel

@Composable
fun ResourcesScreen(
    modifier: Modifier = Modifier, 
    contentPadding: PaddingValues = PaddingValues(16.dp),
    viewModel: ResourcesViewModel = hiltViewModel()
) {
    val selectedType by viewModel.selectedType.collectAsState()
    val resources by viewModel.resources.collectAsState()
    val resourceTypes = viewModel.getResourceTypes()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(id = R.string.resources_title), style = MaterialTheme.typography.titleLarge)
        
        // Resource type selector
        SingleChoiceSegmentedButtonRow {
            resourceTypes.forEach { (type, label) ->
                SegmentedButton(
                    selected = selectedType == type,
                    onClick = { viewModel.selectResourceType(type) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        
        // Resources list
        if (resources.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(resources) { resource ->
                    ResourceCard(resource = resource)
                }
            }
        }
    }
}

@Composable
private fun ResourceCard(resource: com.voitto.data.entity.ResourceEntity) {
    Card {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = resource.name, style = MaterialTheme.typography.titleMedium)
            Text(text = resource.eligibilitySummary, style = MaterialTheme.typography.bodyMedium)
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                resource.applicationLink?.let {
                    Button(onClick = { /* TODO: Open link */ }) { 
                        Text(text = stringResource(id = R.string.action_apply_online)) 
                    }
                }
                resource.phone?.let {
                    OutlinedButton(onClick = { /* TODO: Call phone */ }) { 
                        Text(text = stringResource(id = R.string.action_call_now)) 
                    }
                }
            }
            
            resource.lastVerified?.let { verified ->
                Text(
                    text = "${stringResource(id = R.string.last_verified)}: ${verified}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

