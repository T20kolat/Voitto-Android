package com.voitto.ui.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.voitto.R

@Composable
fun ChallengeScreen(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(16.dp)) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(id = R.string.challenge_of_week), style = MaterialTheme.typography.titleLarge)
        Card {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Soodaton viikko", style = MaterialTheme.typography.titleMedium)
                Text(text = stringResource(id = R.string.estimated_saving) + ": 7–14 € / vko", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { }) { Text(text = stringResource(id = R.string.mark_done)) }
            }
        }
    }
}

