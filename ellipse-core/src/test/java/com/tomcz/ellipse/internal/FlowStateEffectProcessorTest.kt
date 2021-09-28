package com.tomcz.ellipse.internal

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal typealias CounterStateProcessor = Processor<CounterEvent, CounterState, CounterEffect>

internal class FlowStateEffectProcessorTest : BaseCoroutineTest() {

    @Test
    fun `test getting default state`() {
        val processor: CounterStateProcessor = processor(CounterState())
        assertEquals(CounterState(), processor.state.value)
    }

    @Test
    fun `test default state and prepare`() {
        val processor: CounterStateProcessor =
            processor(CounterState(), prepare = { flow { emit(IncreasePartialState) } })
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test state change after event`() {
        val processor: CounterStateProcessor = processor(
            initialState = CounterState(),
            onEvent = { flow { emit(IncreasePartialState) } }
        )
        assertEquals(CounterState(0), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test prepare and state change after event`() {
        val processor: CounterStateProcessor = processor(
            initialState = CounterState(),
            prepare = { flow { emit(IncreasePartialState) } },
            onEvent = { flow { emit(IncreasePartialState) } }
        )
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
    }

    @Test
    fun `test effect`() = runBlockingTest {
        val processor: CounterStateProcessor = processor(
            initialState = CounterState(),
            prepare = { flow { emit(IncreasePartialState) } },
            onEvent = {
                sendEffect(CounterEffect)
                emptyFlow()
            }
        )
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancel()
    }

    @Test
    fun `test resubscribing state`() = runBlockingTest {
        val processor: CounterStateProcessor = processor(
            initialState = CounterState(),
            prepare = { flow { emit(IncreasePartialState) } },
            onEvent = { flow { emit(IncreasePartialState) } }
        )
        val stateEvents = mutableListOf<CounterState>()
        val job = launch { processor.state.collect { stateEvents.add(it) } }
        assertEquals(listOf(CounterState(1)), stateEvents)
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterState(1), CounterState(2)), stateEvents)
        job.cancel()

        val resubscribedEvents = mutableListOf<CounterState>()
        val resubscribedJob = launch { processor.state.collect { resubscribedEvents.add(it) } }
        assertEquals(listOf(CounterState(2)), resubscribedEvents)
        resubscribedJob.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runBlockingTest {
        val processor: CounterStateProcessor = processor(
            initialState = CounterState(),
            prepare = { flow { emit(IncreasePartialState) } },
            onEvent = {
                sendEffect(CounterEffect)
                flow { emit(IncreasePartialState) }
            }
        )
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancel()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        assertEquals(CounterState(2), processor.state.value)
        emptyEffectJob.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runBlockingTest {
        val processor: CounterStateProcessor = processor(
            initialState = CounterState(),
            onEvent = { sendEffect(CounterEffect); emptyFlow() }
        )
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect, CounterEffect), effects)
        effectJob.cancel()
        effect2Job.cancel()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancel()
    }
}
