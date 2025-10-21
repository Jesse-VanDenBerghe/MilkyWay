package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.BurpingCard
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.FeedingCard
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.SetupCard
import com.jessevandenberghe.milkyway.ui.screens.timer.components.cards.SummaryCard
import com.jessevandenberghe.milkyway.utils.HandleSystemBackButton
import com.jessevandenberghe.milkyway.utils.InitializeBrightnessControl
import com.jessevandenberghe.milkyway.utils.setBrightness
import com.jessevandenberghe.milkyway.ui.utils.handleHorizontalSwipe
import com.jessevandenberghe.milkyway.ui.utils.isHorizontalDrag
import com.jessevandenberghe.milkyway.ui.utils.isSwipeUp
import com.jessevandenberghe.milkyway.ui.utils.isSwipeDown

import com.jessevandenberghe.milkyway.ui.screens.timer.components.BrightnessSlider
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    onBack: () -> Unit = {},
    viewModel: TimerViewModel = viewModel { TimerViewModel() }
) {
    val state by viewModel.state.collectAsState()
    var brightness by remember { mutableStateOf(1f) } // 0f to 1f
    var isAdjustingBrightness by remember { mutableStateOf(false) }

    // Reset session state when screen is first shown
    LaunchedEffect(Unit) {
        viewModel.resetSession()
    }
    
    // Initialize brightness control (needed for Android to get Activity context)
    InitializeBrightnessControl()
    
    // Handle system back button
    HandleSystemBackButton(
        onBack = {
            if (state.timingStep == TimingStep.SETUP) {
                onBack()
            } else {
                viewModel.previousStep()
            }
        }
    )

    LaunchedEffect(Unit) {
        while (true) {
            if (isAdjustingBrightness) {
                delay(2000) // Hide after 2 seconds
                isAdjustingBrightness = false
            } else {
                delay(100) // Check periodically
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(state.timingStep) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        // Horizontal drag for brightness
                        if (isHorizontalDrag(dragAmount)) {
                            handleHorizontalSwipe(
                                dragAmount = dragAmount,
                                size = size,
                                currentBrightness = brightness,
                                onBrightnessChange = { newBrightness ->
                                    brightness = newBrightness
                                    setBrightness(brightness * brightness)
                                },
                                onStartAdjusting = { isAdjustingBrightness = true }
                            )
                        }
                        // Vertical drag for step navigation
                        else {
                            when {
                                isSwipeUp(dragAmount) -> viewModel.nextStep()
                                isSwipeDown(dragAmount) -> {
                                    if (state.timingStep == TimingStep.SETUP) {
                                        onBack()
                                    } else {
                                        viewModel.previousStep()
                                    }
                                }
                            }
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
                finalBottleRemainingMillilitersInput = state.finalBottleRemainingMillilitersInput,
                finalBottleRemainingMilliliters = state.finalBottleRemainingMilliliters,
                sessionQuality = state.sessionQuality,
                drinkingSpeed = state.drinkingSpeed,
                onFinalBottleRemainingMillilitersInputChange = { viewModel.updateFinalBottleRemainingMillilitersInput(it) },
                onSessionQualityChange = { viewModel.updateSessionQuality(it) },
                onDrinkingSpeedChange = { viewModel.updateDrinkingSpeed(it) },
                isExpanded = state.timingStep == TimingStep.SUMMARY,
                showScores = state.timingStep == TimingStep.FINISHED,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            AnimatedVisibility(visible = state.timingStep == TimingStep.FINISHED){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🎉",
                            style = MaterialTheme.typography.displayLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Session Complete!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Great job! 👶",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🥛",
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Milk Consumed",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${state.milkConsumed} ml",
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                viewModel.saveSession()
                                onBack()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "Save Session",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
        BrightnessSlider(
            brightness = brightness,
            isVisible = isAdjustingBrightness
        )
    }
}
