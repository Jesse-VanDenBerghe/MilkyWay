package com.jessevandenberghe.milkyway.ui.screens.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IdleControls(
    onStartFeeding: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onStartFeeding,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Start Feeding",
            fontSize = 18.sp
        )
    }
}

@Composable
fun FeedingControls(
    onStop: () -> Unit,
    onStartBurping: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onStartBurping,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Start Burping",
                fontSize = 18.sp
            )
        }
        OutlinedButton(
            onClick = onStop,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Stop",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun BurpingControls(
    onStop: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Finish",
                fontSize = 18.sp
            )
        }
        OutlinedButton(
            onClick = onStop,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Stop",
                fontSize = 18.sp
            )
        }
    }
}
