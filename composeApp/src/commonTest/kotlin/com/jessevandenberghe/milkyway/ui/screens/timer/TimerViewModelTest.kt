package com.jessevandenberghe.milkyway.ui.screens.timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for TimerViewModel focusing on state transitions.
 * Timer progression testing is complex due to infinite coroutine loops.
 * The core business logic (TimerState) is tested in TimerStateTest.
 */
class TimerViewModelTest {

    @Test
    fun initialState_isSetup() {
        val viewModel = TimerViewModel()

        val state = viewModel.state.value

        assertEquals(TimingStep.SETUP, state.timingStep)
        assertEquals(0.seconds, state.elapsedFeedingTime)
        assertEquals(0.seconds, state.elapsedBurpingTime)
    }

    @Test
    fun startFeeding_changesStateToFeeding() {
        val viewModel = TimerViewModel()

        viewModel.startFeeding()

        assertEquals(TimingStep.FEEDING, viewModel.state.value.timingStep)
    }

    @Test
    fun stopFeeding_changesStateToBurping() {
        val viewModel = TimerViewModel()
        viewModel.startFeeding()

        viewModel.stopFeeding()

        assertEquals(TimingStep.BURPING, viewModel.state.value.timingStep)
    }

    @Test
    fun startBurping_changesStateToBurping() {
        val viewModel = TimerViewModel()

        viewModel.startBurping()

        assertEquals(TimingStep.BURPING, viewModel.state.value.timingStep)
    }

    @Test
    fun stopBurping_changesStateToSummary() {
        val viewModel = TimerViewModel()
        viewModel.startBurping()

        viewModel.stopBurping()

        assertEquals(TimingStep.SUMMARY, viewModel.state.value.timingStep)
    }

    @Test
    fun startFeeding_whenAlreadyFeeding_doesNotChangeState() {
        val viewModel = TimerViewModel()
        viewModel.startFeeding()

        // Try to start again
        viewModel.startFeeding()

        // Should still be in FEEDING state
        assertEquals(TimingStep.FEEDING, viewModel.state.value.timingStep)
    }

    @Test
    fun startBurping_whenAlreadyBurping_doesNotChangeState() {
        val viewModel = TimerViewModel()
        viewModel.startBurping()

        // Try to start again
        viewModel.startBurping()

        // Should still be in BURPING state
        assertEquals(TimingStep.BURPING, viewModel.state.value.timingStep)
    }

}
