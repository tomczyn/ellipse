package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> processorTest(
    processor: () -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: E,
    thenStates: TestResultContext<S>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    given = given,
    whenEvents = listOf(whenEvent),
    thenStates = thenStates,
    thenEffects = thenEffects
)

@ExperimentalCoroutinesApi
fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> processorTest(
    processor: () -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<E> = emptyList(),
    thenStates: TestResultContext<S>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
) = runTest {
    processorTest(
        processor = processor,
        given = given,
        whenEvents = whenEvents,
        thenStates = thenStates,
        thenEffects = thenEffects
    )
}

@ExperimentalCoroutinesApi
suspend fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestScope.processorTest(
    processor: () -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: E,
    thenStates: TestResultContext<S>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    given = given,
    whenEvents = listOf(whenEvent),
    thenStates = thenStates,
    thenEffects = thenEffects
)

@ExperimentalCoroutinesApi
suspend fun <E : Any, S : Any, EF : Any, T : Processor<E, S, EF>> TestScope.processorTest(
    processor: () -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<E> = emptyList(),
    thenStates: TestResultContext<S>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
) {
    given()
    val (states, effects) = getStatesAndEffects(processor, whenEvents)
    val stateContext = TestResultContext(states, testScheduler)
    val effectsContext = TestResultContext(effects, testScheduler)
    stateContext.thenStates()
    effectsContext.thenEffects()
}
