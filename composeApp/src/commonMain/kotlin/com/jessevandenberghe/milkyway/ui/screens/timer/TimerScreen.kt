package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.BurpingCard
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.FeedingCard
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.SetupCard
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.SummaryCard
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel { TimerViewModel() }
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(state.timingStep) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()
                    // Swipe up detection (dragAmount is negative when swiping up)
                    if (dragAmount < -50 && abs(dragAmount) > 50) {
                        viewModel.nextStep()
                    }
                    // Swipe down detection (dragAmount is positive when swiping down)
                    else if (dragAmount > 50 && abs(dragAmount) > 50) {
                        viewModel.previousStep()
                    }
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SetupCard(
            bottleTotalMillilitersInput = state.bottleTotalMillilitersInput,
            bottleTotalTimeInput = state.bottleTotalTimeInput,
            onBottleTotalMillilitersInputChange = { viewModel.updateBottleTotalMillilitersInput(it) },
            onBottleTotalTimeInputChange = { viewModel.updateBottleTotalTimeInput(it) },
            isExpanded = state.timingStep == TimingStep.SETUP,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        FeedingCard(
            elapsedTime = state.elapsedFeedingTime,
            bottleTotalMilliliters = state.bottleTotalMilliliters,
            bottleRemainingMilliliters = state.bottleRemainingMilliliters,
            isActive = state.timingStep == TimingStep.FEEDING,
            isExpanded = state.timingStep == TimingStep.FEEDING,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BurpingCard(
            elapsedTime = state.elapsedBurpingTime,
            isActive = state.timingStep == TimingStep.BURPING,
            isExpanded = state.timingStep == TimingStep.BURPING,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SummaryCard(
            finalBottleRemainingMilliliters = state.finalBottleRemainingMilliliters,
            sessionQuality = state.sessionQuality,
            drinkingSpeed = state.drinkingSpeed,
            onFinalBottleRemainingMillilitersChange = { viewModel.updateFinalBottleRemainingMilliliters(it) },
            onSessionQualityChange = { viewModel.updateSessionQuality(it) },
            onDrinkingSpeedChange = { viewModel.updateDrinkingSpeed(it) },
            isExpanded = state.timingStep == TimingStep.SUMMARY,
            showScores = state.timingStep == TimingStep.FINISHED,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}
