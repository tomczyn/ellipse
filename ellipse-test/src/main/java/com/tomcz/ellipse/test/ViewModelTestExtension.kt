package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    given = given,
    whenEvents = listOf(whenEvent),
    thenStates = thenStates,
    thenEffects = thenEffects
)

@ExperimentalCoroutinesApi
fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    thenStates: TestResultContext<ST>.() -> Unit = {},
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
suspend fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    given = given,
    whenEvents = listOf(whenEvent),
    thenStates = thenStates,
    thenEffects = thenEffects
)

@ExperimentalCoroutinesApi
suspend fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {}
) {
    given()
    val (states, effects) = getStatesAndEffects(processor, whenEvents)
    val stateContext = TestResultContext(states, testScheduler)
    val effectsContext = TestResultContext(effects, testScheduler)
    stateContext.thenStates()
    effectsContext.thenEffects()
}
