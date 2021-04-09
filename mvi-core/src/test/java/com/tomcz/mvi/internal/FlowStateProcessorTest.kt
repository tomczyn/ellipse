package com.tomcz.mvi.internal

import com.tomcz.mvi.Intent
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.common.stateProcessor
import com.tomcz.mvi.test.BaseCoroutineTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FlowStateProcessorTest : BaseCoroutineTest() {

    object CounterEvent
    data class CounterState(val counter: Int = 0)
    object IncreasePartialState : Intent<CounterState> {
        override fun reduce(oldState: CounterState): CounterState =
            oldState.copy(counter = oldState.counter + 1)
    }

    @Test
    fun `test getting default state`() {
        val processor: StateProcessor<CounterEvent, CounterState> = stateProcessor(CounterState())
        assertEquals(CounterState(), processor.state.value)
    }

    @Test
    fun `test default state and prepare`() {
        val processor: StateProcessor<CounterEvent, CounterState> =
            stateProcessor(CounterState(), prepare = { flow { emit(IncreasePartialState) } })
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test state change after event`() {
        val processor: StateProcessor<CounterEvent, CounterState> =
            stateProcessor(CounterState()) { flow { emit(IncreasePartialState) } }
        assertEquals(CounterState(0), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(1), processor.state.value)
    }

    @Test
    fun `test prepare and state change after event`() {
        val processor: StateProcessor<CounterEvent, CounterState> =
            stateProcessor(CounterState(), prepare = { flow { emit(IncreasePartialState) } }) {
                flow { emit(IncreasePartialState) }
            }
        assertEquals(CounterState(1), processor.state.value)
        processor.sendEvent(CounterEvent)
        assertEquals(CounterState(2), processor.state.value)
    }

    @Test
    fun `test resubscribing`() = runBlockingTest {
        val processor: StateProcessor<CounterEvent, CounterState> =
            stateProcessor(CounterState(), prepare = { flow { emit(IncreasePartialState) } }) {
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
    fun `test having multiple subscribers`() {
        val processor: StateProcessor<CounterEvent, CounterState> =
            stateProcessor(CounterState(), prepare = { flow { emit(IncreasePartialState) } }) {
                flow { emit(IncreasePartialState) }
            }
        val stateEvents = mutableListOf<CounterState>()
        val job1 = launch { processor.state.collect { stateEvents.add(it) } }
        val job2 = launch { processor.state.collect { stateEvents.add(it) } }
        assertEquals(listOf(CounterState(1), CounterState(1)), stateEvents)
        processor.sendEvent(CounterEvent)
        assertEquals(
            listOf(
                CounterState(1),
                CounterState(1),
                CounterState(2),
                CounterState(2)
            ),
            stateEvents
        )
        job1.cancel()
        job2.cancel()

        val resubscribedEvents = mutableListOf<CounterState>()
        val resubscribedJob = launch { processor.state.collect { resubscribedEvents.add(it) } }
        assertEquals(listOf(CounterState(2)), resubscribedEvents)
        resubscribedJob.cancel()
    }
}
