package com.tomcz.mvi_test

import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest

@JvmName("stateEventProcessorTest")
fun <E : Any, S : Any, T : StateProcessor<E, S>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvent: E,
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) }
): Unit = processorTest(given, listOf(whenEvent), thenStates)

@JvmName("stateEventsProcessorTest")
fun <E : Any, S : Any, T : StateProcessor<E, S>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvents: List<E> = emptyList(),
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) }
): Unit = runBlockingTest {
    StateProcessorTest(given(), whenEvents, this).apply { states.thenStates() }
}

@JvmName("stateEffectEventProcessorTest")
fun <E : Any, S : Any, EF : Any, T : StateEffectProcessor<E, S, EF>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvent: E,
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) },
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = processorTest(given, listOf(whenEvent), thenStates, thenEffects)

@JvmName("stateEffectEventsProcessorTest")
fun <E : Any, S : Any, EF : Any, T : StateEffectProcessor<E, S, EF>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvents: List<E> = emptyList(),
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) },
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = runBlockingTest {
    StateEffectProcessorTest(given(), whenEvents, this).apply {
        states.thenStates()
        effects.thenEffects()
    }
}

@JvmName("effectEventProcessorTest")
fun <T : EffectProcessor<EV, EF>, EV : Any, EF : Any> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvent: EV,
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = processorTest(given, listOf(whenEvent), thenEffects)

@JvmName("effectEventsProcessorTest")
fun <T : EffectProcessor<EV, EF>, EV : Any, EF : Any> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvents: List<EV> = emptyList(),
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = runBlockingTest {
    EffectProcessorTest(given(), whenEvents, this).apply { effects.thenEffects() }
}
