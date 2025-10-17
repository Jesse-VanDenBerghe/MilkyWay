package com.jessevandenberghe.milkyway.ui.screens.timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TimerStateTest {

    @Test
    fun initialState_hasDefaultValues() {
        val state = TimerState()

        assertEquals(0.seconds, state.elapsedFeedingTime)
        assertEquals(0.seconds, state.elapsedBurpingTime)
        assertEquals(TimingStep.IDLE, state.timingStep)
    }

    @Test
    fun copy_withFeedingTime_updatesOnlyFeedingTime() {
        val state = TimerState()
        val newState = state.copy(elapsedFeedingTime = 2.minutes)

        assertEquals(2.minutes, newState.elapsedFeedingTime)
        assertEquals(0.seconds, newState.elapsedBurpingTime)
        assertEquals(TimingStep.IDLE, newState.timingStep)
    }

    @Test
    fun copy_withBurpingTime_updatesOnlyBurpingTime() {
        val state = TimerState()
        val newState = state.copy(elapsedBurpingTime = 30.seconds)

        assertEquals(0.seconds, newState.elapsedFeedingTime)
        assertEquals(30.seconds, newState.elapsedBurpingTime)
        assertEquals(TimingStep.IDLE, newState.timingStep)
    }

    @Test
    fun copy_withTimingStep_updatesOnlyTimingStep() {
        val state = TimerState()
        val newState = state.copy(timingStep = TimingStep.FEEDING)

        assertEquals(0.seconds, newState.elapsedFeedingTime)
        assertEquals(0.seconds, newState.elapsedBurpingTime)
        assertEquals(TimingStep.FEEDING, newState.timingStep)
    }

    @Test
    fun copy_withMultipleProperties_updatesAll() {
        val state = TimerState()
        val newState = state.copy(
            elapsedFeedingTime = 5.minutes,
            elapsedBurpingTime = 1.minutes,
            timingStep = TimingStep.BURPING
        )

        assertEquals(5.minutes, newState.elapsedFeedingTime)
        assertEquals(1.minutes, newState.elapsedBurpingTime)
        assertEquals(TimingStep.BURPING, newState.timingStep)
    }

    @Test
    fun equality_sameValues_areEqual() {
        val state1 = TimerState(
            elapsedFeedingTime = 2.minutes,
            elapsedBurpingTime = 30.seconds,
            timingStep = TimingStep.FEEDING
        )
        val state2 = TimerState(
            elapsedFeedingTime = 2.minutes,
            elapsedBurpingTime = 30.seconds,
            timingStep = TimingStep.FEEDING
        )

        assertEquals(state1, state2)
    }

    @Test
    fun equality_differentValues_areNotEqual() {
        val state1 = TimerState(elapsedFeedingTime = 2.minutes)
        val state2 = TimerState(elapsedFeedingTime = 3.minutes)

        assertNotEquals(state1, state2)
    }

    @Test
    fun immutability_copyDoesNotModifyOriginal() {
        val original = TimerState(elapsedFeedingTime = 1.minutes)
        val modified = original.copy(elapsedFeedingTime = 2.minutes)

        assertEquals(1.minutes, original.elapsedFeedingTime)
        assertEquals(2.minutes, modified.elapsedFeedingTime)
    }
}
