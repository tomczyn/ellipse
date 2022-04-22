package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
private val defaultAfter: TestScope.() -> Unit = {
    advanceUntilIdle()
}

@ExperimentalCoroutinesApi
fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> processorTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    context = context,
    given = given,
    whenEvents = listOf(whenEvent),
    afterPrepare = afterPrepare,
    afterEvents = afterEvents,
    thenStates = thenStates,
    thenEffects = thenEffects,
    cleanup = cleanup
)

@ExperimentalCoroutinesApi
fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> processorTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
) = runTest(context) {
    processorTest(
        processor = processor,
        given = given,
        whenEvents = whenEvents,
        afterPrepare = afterPrepare,
        afterEvents = afterEvents,
        thenStates = thenStates,
        thenEffects = thenEffects,
        cleanup = cleanup
    )
}

@ExperimentalCoroutinesApi
fun <EV : Any, EF : Any, T : Processor<EV, Nothing, EF>> processorTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    context = context,
    given = given,
    whenEvents = listOf(whenEvent),
    afterPrepare = afterPrepare,
    afterEvents = afterEvents,
    thenEffects = thenEffects,
    cleanup = cleanup
)

@ExperimentalCoroutinesApi
fun <EV : Any, EF : Any, T : Processor<EV, Nothing, EF>> processorTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
) = runTest(context) {
    processorTest(
        processor = processor,
        given = given,
        whenEvents = whenEvents,
        afterPrepare = afterPrepare,
        afterEvents = afterEvents,
        thenEffects = thenEffects,
        cleanup = cleanup
    )
}

@ExperimentalCoroutinesApi
suspend fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    given = given,
    whenEvents = listOf(whenEvent),
    afterPrepare = afterPrepare,
    afterEvents = afterEvents,
    thenStates = thenStates,
    thenEffects = thenEffects,
    cleanup = cleanup,
)

@ExperimentalCoroutinesApi
suspend fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
) {
    given()
    val (states, effects) = getStatesAndEffects(
        processorFactory = processor,
        events = whenEvents,
        afterPrepare = afterPrepare,
        afterEvents = afterEvents,
    )
    val stateContext = TestResultContext(states, testScheduler)
    val effectsContext = TestResultContext(effects, testScheduler)
    stateContext.thenStates()
    effectsContext.thenEffects()
    cleanup()
}

@ExperimentalCoroutinesApi
suspend fun <EV : Any, EF : Any, T : Processor<EV, Nothing, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
): Unit = processorTest(
    processor = processor,
    given = given,
    whenEvents = listOf(whenEvent),
    afterPrepare = afterPrepare,
    afterEvents = afterEvents,
    thenEffects = thenEffects,
    cleanup = cleanup,
)

@ExperimentalCoroutinesApi
suspend fun <EV : Any, EF : Any, T : Processor<EV, Nothing, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = {}
) {
    given()
    val effects = getEffects(
        processorFactory = processor,
        events = whenEvents,
        afterPrepare = afterPrepare,
        afterEvents = afterEvents,
    )
    val effectsContext = TestResultContext(effects, testScheduler)
    effectsContext.thenEffects()
    cleanup()
}
