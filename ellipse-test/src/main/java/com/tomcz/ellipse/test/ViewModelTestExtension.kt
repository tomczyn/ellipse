package com.tomcz.ellipse.test

import com.tomcz.ellipse.EffectProcessor
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.StateProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
@JvmName("stateEventProcessorTest")
fun <E : Any, S : Any, T : StateProcessor<E, S>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvent: E,
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) }
): Unit = processorTest(given, listOf(whenEvent), thenStates)

@ExperimentalCoroutinesApi
@JvmName("stateEventsProcessorTest")
fun <E : Any, S : Any, T : StateProcessor<E, S>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvents: List<E> = emptyList(),
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) }
): Unit = runBlockingTest {
    StateProcessorTest(given(), whenEvents, this).apply { states.thenStates() }
}

@ExperimentalCoroutinesApi
@JvmName("stateEffectEventProcessorTest")
fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvent: E,
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) },
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = processorTest(given, listOf(whenEvent), thenStates, thenEffects)

@ExperimentalCoroutinesApi
@JvmName("stateEffectEventsProcessorTest")
fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestCoroutineScope.processorTest(
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

@ExperimentalCoroutinesApi
@JvmName("effectEventProcessorTest")
fun <T : EffectProcessor<EV, EF>, EV : Any, EF : Any> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvent: EV,
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = processorTest(given, listOf(whenEvent), thenEffects)

@ExperimentalCoroutinesApi
@JvmName("effectEventsProcessorTest")
fun <T : EffectProcessor<EV, EF>, EV : Any, EF : Any> TestCoroutineScope.processorTest(
    given: () -> T,
    whenEvents: List<EV> = emptyList(),
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = runBlockingTest {
    EffectProcessorTest(given(), whenEvents, this).apply { effects.thenEffects() }
}
