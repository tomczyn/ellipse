package com.tomcz.ellipse.internal

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.setState
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
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
            scope.processor(CounterState(), prepare = { setState(IncreasePartial) })
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(CounterState()) { setState(IncreasePartial) }
        assertEquals(CounterState(0), processor.state.value)
        processor.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test prepare and state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(
                CounterState(), prepare = { setState(IncreasePartial) }
            ) { setState(IncreasePartial) }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        scope.cancel()
    }

    @Test
    fun `test another prepare and state change after event`() = runTest {
        val scope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            scope.processor(
                CounterState(), prepare = { setState(flow { emit(IncreasePartial) }) }
            ) { setState(flow { emit(IncreasePartial) }) }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
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
                prepare = { setState(IncreasePartial) }
            ) {
                sendEffect(CounterEffect)
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()
        scope.cancel()
    }

    @Test
    fun `test resubscribing state`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { setState(IncreasePartial) }
            ) {
                setState(IncreasePartial)
            }
        val stateEvents = mutableListOf<CounterState>()
        val job = launch { processor.state.collect { stateEvents.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterState(1)), stateEvents)
        processor.sendEvent(CounterEvent)
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
                prepare = { setState(IncreasePartial) }
            ) {
                sendEffect(CounterEffect)
                setState(IncreasePartial)
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(CounterEffect), effects)
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
            processorScope.processor(CounterState()) { sendEffect(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect, CounterEffect), effects)
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
                prepare = { setState(IncreasePartial) }
            ) {
                sendEffect(CounterEffect)
                setState(IncreasePartial)
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        runCurrent()
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        processor.sendEvent(CounterEvent, CounterEvent)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { processor.effect.collect { cachedEffects.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterEffect, CounterEffect), cachedEffects)
        assertEquals(CounterState(4), processor.state.value)
        cachedEffectsJob.cancelAndJoin()
        processorScope.cancel()
    }
}
