package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest

fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestCoroutineScope.processorTest(
    processor: () -> T,
    given: suspend () -> Unit = {},
    whenEvent: E,
    thenStates: ListTester<S>.() -> Unit = {},
    thenEffects: ListTester<EF>.() -> Unit = {}
): Unit = processorTest(processor, given, listOf(whenEvent), thenStates, thenEffects)

fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestCoroutineScope.processorTest(
    processor: () -> T,
    given: suspend () -> Unit = {},
    whenEvents: List<E> = emptyList(),
    thenStates: ListTester<S>.() -> Unit = {},
    thenEffects: ListTester<EF>.() -> Unit = {}
): Unit = runBlockingTest {
    given()
    val (states, effects) = processTestData(processor, whenEvents, this)
    states.thenStates()
    effects.thenEffects()
}
