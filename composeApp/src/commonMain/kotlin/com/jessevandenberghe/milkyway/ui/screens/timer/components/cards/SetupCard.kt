package com.jessevandenberghe.milkyway.ui.screens.timer.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jessevandenberghe.milkyway.ui.screens.timer.TimerState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun SetupCard(
    bottleTotalMillilitersInput: String,
    bottleTotalTimeInput: String,
    onBottleTotalMillilitersInputChange: (String) -> Unit,
    onBottleTotalTimeInputChange: (String) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    var millilitersFocused by remember { mutableStateOf(false) }
    var timeFocused by remember { mutableStateOf(false) }
    
    ExpandingCard(
        isExpanded = isExpanded,
        isActive = false,
        activeContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier,
        expandedContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bottle Setup",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Bottle total milliliters input
                OutlinedTextField(
                    value = bottleTotalMillilitersInput,
                    onValueChange = onBottleTotalMillilitersInputChange,
                    label = { Text("Bottle Size") },
                    placeholder = { Text(TimerState.DEFAULT_BOTTLE_TOTAL_MILLILITERS_INPUT) },
                    suffix = { Text("ml") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused && !millilitersFocused) {
                                millilitersFocused = true
                                if (bottleTotalMillilitersInput == TimerState.DEFAULT_BOTTLE_TOTAL_MILLILITERS_INPUT) {
                                    onBottleTotalMillilitersInputChange("")
                                }
                            } else if (!focusState.isFocused) {
                                millilitersFocused = false
                                if (bottleTotalMillilitersInput.isEmpty()) {
                                    onBottleTotalMillilitersInputChange(TimerState.DEFAULT_BOTTLE_TOTAL_MILLILITERS_INPUT)
                                }
                            }
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bottle total time input (in minutes)
                OutlinedTextField(
                    value = bottleTotalTimeInput,
                    onValueChange = onBottleTotalTimeInputChange,
                    label = { Text("Target Time") },
                    placeholder = { Text(TimerState.DEFAULT_BOTTLE_TOTAL_TIME_INPUT) },
                    suffix = { Text("min") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused && !timeFocused) {
                                timeFocused = true
                                if (bottleTotalTimeInput == TimerState.DEFAULT_BOTTLE_TOTAL_TIME_INPUT) {
                                    onBottleTotalTimeInputChange("")
                                }
                            } else if (!focusState.isFocused) {
                                timeFocused = false
                                if (bottleTotalTimeInput.isEmpty()) {
                                    onBottleTotalTimeInputChange(TimerState.DEFAULT_BOTTLE_TOTAL_TIME_INPUT)
                                }
                            }
                        }
                )
            }
        },
        collapsedContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bottle Setup",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${bottleTotalMillilitersInput}ml / ${bottleTotalTimeInput}min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    )
}
