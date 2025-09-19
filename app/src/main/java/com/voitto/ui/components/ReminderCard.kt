package com.voitto.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.voitto.R
import com.voitto.ui.components.AnimatedButton

@Composable
fun ReminderCard(
    title: String,
    subtitle: String,
    onConfirm: () -> Unit,
    onSnooze: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnimatedButton(onClick = onConfirm) { 
                    Text(text = stringResource(id = R.string.action_confirm)) 
                }
                AnimatedButton(onClick = onSnooze) { 
                    Text(text = stringResource(id = R.string.action_snooze)) 
                }
                AnimatedButton(onClick = onDismiss) { 
                    Text(text = stringResource(id = R.string.action_dismiss)) 
                }
            }
        }
    }
}

