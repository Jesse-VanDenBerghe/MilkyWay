package com.jessevandenberghe.milkyway.ui.screens.timeline

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessevandenberghe.milkyway.data.repository.SessionRepository
import com.jessevandenberghe.milkyway.ui.screens.timeline.components.TimelineItem

@Composable
fun TimelineScreen(
    onStartSession: () -> Unit = {},
    viewModel: TimelineViewModel = viewModel {
        TimelineViewModel(SessionRepository())
    }
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Feeding Timeline",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onStartSession) {
                Text("Start Session")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Timeline content
        if (state.sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No feeding sessions yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(state.sessions) { index, session ->
                    TimelineItem(
                        session = session,
                        isFirst = index == 0,
                        isLast = index == state.sessions.size - 1
                    )
                }
            }
        }
    }
}
