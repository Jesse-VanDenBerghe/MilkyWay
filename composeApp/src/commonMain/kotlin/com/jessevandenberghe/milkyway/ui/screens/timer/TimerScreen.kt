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
import com.jessevandenberghe.milkyway.utils.InitializeBrightnessControl
import com.jessevandenberghe.milkyway.utils.setBrightness
import kotlin.math.abs

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

    // Initialize brightness control (needed for Android to get Activity context)
    InitializeBrightnessControl()

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

                        val (x, y) = dragAmount

                        // Horizontal drag for brightness
                        if (abs(x) > abs(y)) {
                            isAdjustingBrightness = true
                            // Adjust brightness (swipe right increases, swipe left decreases)
                            val adjustment = x / size.width.toFloat() * 0.5f
                            brightness = (brightness + adjustment).coerceIn(0.0f, 1f)
                            setBrightness(brightness * brightness)
                        }
                        // Vertical drag for step navigation
                        else {
                            // Swipe up detection (y is negative when swiping up)
                            if (y < -50 && abs(y) > 50) {
                                viewModel.nextStep()
                            }
                            // Swipe down detection (y is positive when swiping down)
                            else if (y > 50 && abs(y) > 50) {
                                viewModel.previousStep()
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
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        val milkConsumed = state.bottleTotalMilliliters - (state.finalBottleRemainingMilliliters?.toString()?.toIntOrNull() ?: 0)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🍼 Milk Consumed",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$milkConsumed ml",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                viewModel.saveSession()
                                onBack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Session")
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
