package com.tomcz.ellipse.internal

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.internal.CounterEffect.Effect1
import com.tomcz.ellipse.internal.CounterEffect.Effect2
import com.tomcz.ellipse.internal.CounterEvent.Event1
import com.tomcz.ellipse.util.BaseCoroutineTest
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
internal class FlowProcessorTest : BaseCoroutineTest() {

    @Test
    fun `test getting default state`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(CounterState())
        assertEquals(CounterState(), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test default state and prepare`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(CounterState(), prepare = { flow { emit(IncreasePartialState) } })
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(CounterState()) { flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(0), processor.state.value)
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test prepare and state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(
                CounterState(), prepare = { flow { emit(IncreasePartialState) } }
            ) { flow { emit(IncreasePartialState) } }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test effect`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(Effect1)
                emptyFlow()
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(listOf(Effect1), effects)
        effectJob.cancelAndJoin()
        scope.cancel()
    }

    @Test
    fun `test resubscribing state`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                flow { emit(IncreasePartialState) }
            }
        val stateEvents = mutableListOf<CounterState>()
        val job = launch { processor.state.collect { stateEvents.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterState(1)), stateEvents)
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(listOf(CounterState(1), CounterState(2)), stateEvents)
        job.cancelAndJoin()

        val resubscribedEvents = mutableListOf<CounterState>()
        val resubscribedJob = launch { processor.state.collect { resubscribedEvents.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterState(2)), resubscribedEvents)
        resubscribedJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(Effect1)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(Effect1), effects)
        effectJob.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        runCurrent()
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        assertEquals(CounterState(2), processor.state.value)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(CounterState()) { effects.send(Effect1); emptyFlow() }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        runCurrent()
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(listOf(Effect1, Effect1), effects)
        effectJob.cancelAndJoin()
        effect2Job.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        runCurrent()
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test caching effects when there are no subscribers`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(Effect1)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(Effect1), effects)
        effectJob.cancelAndJoin()

        processor.sendEvent(Event1)
        processor.sendEvent(Event1)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { processor.effect.collect { cachedEffects.add(it) } }
        runCurrent()
        assertEquals(listOf(Effect1, Effect1), cachedEffects)
        assertEquals(CounterState(4), processor.state.value)
        cachedEffectsJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test caching effects for multiple subscribes to different subclasses`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(Effect1)
                effects.send(Effect2)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob1 =
            launch { processor.effect(Effect1::class).collect { effects.add(it) } }
        val effectJob2 =
            launch { processor.effect(Effect2::class).collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(Event1)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(Effect1, Effect2), effects)
        effectJob1.cancelAndJoin()
        effectJob2.cancelAndJoin()

        processor.sendEvent(Event1)
        processor.sendEvent(Event1)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob1 = launch {
            processor.effect(Effect1::class).collect { cachedEffects.add(it) }
        }
        val cachedEffectsJob2 = launch {
            processor.effect(Effect2::class).collect { cachedEffects.add(it) }
        }
        runCurrent()
        assertEquals(listOf(Effect1, Effect2, Effect1, Effect2), cachedEffects)
        assertEquals(CounterState(4), processor.state.value)
        cachedEffectsJob1.cancelAndJoin()
        cachedEffectsJob2.cancelAndJoin()
        processorScope.cancel()
    }
}
