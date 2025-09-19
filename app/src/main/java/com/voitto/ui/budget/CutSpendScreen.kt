package com.voitto.ui.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.voitto.R

@Composable
fun CutSpendScreen(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(16.dp)) {
    val percent = remember { mutableFloatStateOf(0.1f) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(id = R.string.cut_or_spend_title))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { percent.floatValue = 0.05f }) { Text("-5%") }
            OutlinedButton(onClick = { percent.floatValue = 0.10f }) { Text("-10%") }
            OutlinedButton(onClick = { percent.floatValue = 0.15f }) { Text("-15%") }
        }
        Text(text = "Ruoka: leikkaus ${(percent.floatValue * 100).toInt()}%")
        Slider(value = percent.floatValue, onValueChange = { percent.floatValue = it }, valueRange = 0f..0.3f)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { }) { Text(text = stringResource(id = R.string.apply_to_budget)) }
            OutlinedButton(onClick = { percent.floatValue = 0.0f }) { Text(text = stringResource(id = R.string.action_reset)) }
        }
    }
}

