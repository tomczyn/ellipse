package com.tomcz.mvi.internal

import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.common.stateEffectProcessor
import com.tomcz.mvi.util.BaseCoroutineTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FlowStateEffectProcessorTest : BaseCoroutineTest() {

    object CounterEvent
    object CounterEffect
    data class CounterState(val counter: Int = 0)
    object IncreasePartialState : PartialState<CounterState> {
        override fun reduce(oldState: CounterState): CounterState =
            oldState.copy(counter = oldState.counter + 1)
    }

    @Test
    fun `test getting default state`() {
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(CounterState())
        assertEquals(CounterState(), processor.state.value)
    }

    @Test
    fun `test default state and prepare`() {
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(CounterState(), prepare = { flow { emit(IncreasePartialState) } })
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test state change after event`() {
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(CounterState()) { _, _ -> flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(0), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test prepare and state change after event`() {
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(
                CounterState(), prepare = { flow { emit(IncreasePartialState) } }
            ) { _, _ -> flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
    }

    @Test
    fun `test effect`() = runBlockingTest {
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) { effects, _ ->
                effects.send(CounterEffect)
                emptyFlow()
            }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancel()
    }

    @Test
    fun `test resubscribing state`() = runBlockingTest {
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) { _, _ ->
                flow { emit(IncreasePartialState) }
            }
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
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(
                CounterState(),
                prepare = { flow { emit(IncreasePartialState) } }
            ) { effects, _ ->
                effects.send(CounterEffect)
                flow { emit(IncreasePartialState) }
            }
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
        val processor: StateEffectProcessor<CounterEvent, CounterState, CounterEffect> =
            stateEffectProcessor(CounterState()) { effects, _ -> effects.send(CounterEffect); emptyFlow() }
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
