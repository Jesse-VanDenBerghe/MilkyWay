package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessevandenberghe.milkyway.ui.screens.timer.components.BurpingControls
import com.jessevandenberghe.milkyway.ui.screens.timer.components.FeedingControls
import com.jessevandenberghe.milkyway.ui.screens.timer.components.IdleControls
import com.jessevandenberghe.milkyway.ui.screens.timer.components.TimerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel { TimerViewModel() }
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerCard(
            title = "Feeding",
            elapsedTime = state.elapsedFeedingTime,
            isActive = state.timingStep == TimingStep.FEEDING,
            isExpanded = state.timingStep == TimingStep.FEEDING || state.timingStep == TimingStep.IDLE,
            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
            activeTimeColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TimerCard(
            title = "Burping",
            elapsedTime = state.elapsedBurpingTime,
            isActive = state.timingStep == TimingStep.BURPING,
            isExpanded = state.timingStep == TimingStep.BURPING || state.timingStep == TimingStep.IDLE,
            activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            activeTimeColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        when (state.timingStep) {
            TimingStep.IDLE -> {
                IdleControls(
                    onStartFeeding = { viewModel.startFeeding() }
                )
            }

            TimingStep.FEEDING -> {
                FeedingControls(
                    onStop = { viewModel.stopFeeding() },
                    onStartBurping = {
                        viewModel.stopFeeding()
                        viewModel.startBurping()
                    }
                )
            }

            TimingStep.BURPING -> {
                BurpingControls(
                    onStop = { viewModel.stopBurping() },
                    onFinish = {
                        viewModel.stopBurping()
                        // TODO: Navigate to summary screen
                    }
                )
            }

            TimingStep.FINISHED -> {
                // TODO: Show summary or navigate
            }
        }
    }
}
