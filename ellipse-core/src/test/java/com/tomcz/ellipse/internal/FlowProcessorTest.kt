package com.tomcz.ellipse.internal

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class FlowProcessorTest : BaseCoroutineTest() {

    @Test
    fun `test getting default state`() {
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processor(CounterState())
        assertEquals(CounterState(), processor.state.value)
    }

    @Test
    fun `test default state and prepare`() {
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processor(CounterState(), prepare = { flow { emit(IncreasePartialState) } })
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test state change after event`() {
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processor(CounterState()) { flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(0), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test prepare and state change after event`() {
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processor(
                CounterState(), prepare = { flow { emit(IncreasePartialState) } }
            ) { flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
    }

    @Test
    fun `test effect`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(CounterEffect)
                emptyFlow()
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test resubscribing state`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                flow { emit(IncreasePartialState) }
            }
        val stateEvents = mutableListOf<CounterState>()
        val job = launch { processor.state.collect { stateEvents.add(it) } }
        assertEquals(listOf(CounterState(1)), stateEvents)
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterState(1), CounterState(2)), stateEvents)
        job.cancelAndJoin()

        val resubscribedEvents = mutableListOf<CounterState>()
        val resubscribedJob = launch { processor.state.collect { resubscribedEvents.add(it) } }
        assertEquals(listOf(CounterState(2)), resubscribedEvents)
        resubscribedJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(CounterEffect)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        assertEquals(CounterState(2), processor.state.value)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(CounterState()) { effects.send(CounterEffect); emptyFlow() }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect, CounterEffect), effects)
        effectJob.cancelAndJoin()
        effect2Job.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test caching effects when there are no subscribers`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: Processor<CounterEvent, CounterState, CounterEffect> =
            processorScope.processor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) {
                effects.send(CounterEffect)
                flow { emit(IncreasePartialState) }
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        processor.sendEvent(CounterEvent)
        processor.sendEvent(CounterEvent)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { processor.effect.collect { cachedEffects.add(it) } }
        assertEquals(listOf(CounterEffect, CounterEffect), cachedEffects)
        assertEquals(CounterState(4), processor.state.value)
        cachedEffectsJob.cancelAndJoin()
        processorScope.cancel()
    }
}
