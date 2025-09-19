package com.voitto.ui.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.voitto.R
import com.voitto.ui.components.ReminderCard

@Composable
fun RemindersScreen(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(16.dp)) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReminderCard(
            title = stringResource(id = R.string.predicted_expense_title),
            subtitle = stringResource(id = R.string.bill_vehicle_tax) + " — 180–220 €",
            onConfirm = {},
            onSnooze = {},
            onDismiss = {}
        )
        ReminderCard(
            title = stringResource(id = R.string.predicted_expense_title),
            subtitle = stringResource(id = R.string.bill_inspection) + " — 60–100 €",
            onConfirm = {},
            onSnooze = {},
            onDismiss = {}
        )
    }
}

