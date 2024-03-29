package com.tomczyn.ellipse.internal

import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.common.ellipse
import com.tomczyn.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class EllipseImplTest : BaseCoroutineTest() {

    @Test
    fun `test getting default state`() = runTest {
        val scope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            scope.ellipse(CounterState())
        assertEquals(CounterState(), ellipse.state.value)
        scope.cancel()
    }

    @Test
    fun `test default state and prepare`() = runTest {
        val scope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            scope.ellipse(CounterState(), prepare = { flow { emit(IncreasePartialState) } })
        runCurrent()
        assertEquals(CounterState(1), ellipse.state.value)
        scope.cancel()
    }

    @Test
    fun `test state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            scope.ellipse(CounterState()) { flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(0), ellipse.state.value)
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(1), ellipse.state.value)
        scope.cancel()
    }

    @Test
    fun `test prepare and state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            scope.ellipse(
                CounterState(), prepare = { flow { emit(IncreasePartialState) } }
            ) { flow { emit(IncreasePartialState) } }
        runCurrent()
        assertEquals(CounterState(1), ellipse.state.value)
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(2), ellipse.state.value)
        scope.cancel()
    }

    @Test
    fun `test effect`() = runTest {
        val scope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            scope.ellipse(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(CounterEffect)
                emptyFlow()
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()
        scope.cancel()
    }

    @Test
    fun `test resubscribing state`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            ellipseScope.ellipse(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                flow { emit(IncreasePartialState) }
            }
        val stateEvents = mutableListOf<CounterState>()
        val job = launch { ellipse.state.collect { stateEvents.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterState(1)), stateEvents)
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterState(1), CounterState(2)), stateEvents)
        job.cancelAndJoin()

        val resubscribedEvents = mutableListOf<CounterState>()
        val resubscribedJob = launch { ellipse.state.collect { resubscribedEvents.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterState(2)), resubscribedEvents)
        resubscribedJob.cancelAndJoin()
        ellipseScope.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            ellipseScope.ellipse(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(CounterEffect)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), ellipse.state.value)
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(2), ellipse.state.value)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { ellipse.effect.collect { emptyEffects.add(it) } }
        runCurrent()
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        assertEquals(CounterState(2), ellipse.state.value)
        emptyEffectJob.cancelAndJoin()
        ellipseScope.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            ellipseScope.ellipse(CounterState()) { effects.send(CounterEffect); emptyFlow() }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        val effect2Job = launch { ellipse.effect.collect { effects.add(it) } }
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect, CounterEffect), effects)
        effectJob.cancelAndJoin()
        effect2Job.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { ellipse.effect.collect { emptyEffects.add(it) } }
        runCurrent()
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        ellipseScope.cancel()
    }

    @Test
    fun `test caching effects when there are no subscribers`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: Ellipse<CounterEvent, CounterState, CounterEffect> =
            ellipseScope.ellipse(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(CounterEffect)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), ellipse.state.value)
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(2), ellipse.state.value)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        ellipse.sendEvent(CounterEvent)
        ellipse.sendEvent(CounterEvent)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { ellipse.effect.collect { cachedEffects.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterEffect, CounterEffect), cachedEffects)
        assertEquals(CounterState(4), ellipse.state.value)
        cachedEffectsJob.cancelAndJoin()
        ellipseScope.cancel()
    }
}
