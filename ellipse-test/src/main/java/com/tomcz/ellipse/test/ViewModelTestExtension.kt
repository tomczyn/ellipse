package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
@JvmName("stateEffectEventProcessorTest")
fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestCoroutineScope.processorTest(
    processor: () -> T,
    given: suspend () -> Unit = {},
    whenEvent: E,
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) },
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = processorTest(processor, given, listOf(whenEvent), thenStates, thenEffects)

@ExperimentalCoroutinesApi
@JvmName("stateEffectEventsProcessorTest")
fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestCoroutineScope.processorTest(
    processor: () -> T,
    given: suspend () -> Unit = {},
    whenEvents: List<E> = emptyList(),
    thenStates: ListTester<S>.() -> Unit = { assertSize(0) },
    thenEffects: ListTester<EF>.() -> Unit = { assertSize(0) }
): Unit = runBlockingTest {
    given()
    StateEffectProcessorTest(processor, whenEvents, this).apply {
        states.thenStates()
        effects.thenEffects()
    }
}
