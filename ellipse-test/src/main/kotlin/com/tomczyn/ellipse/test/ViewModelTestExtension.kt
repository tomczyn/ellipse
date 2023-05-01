package com.tomczyn.ellipse.test

import com.tomczyn.ellipse.Processor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
private val defaultAfter: TestScope.() -> Unit = { advanceUntilIdle() }

@ExperimentalCoroutinesApi
private val defaultCleanup: TestScope.() -> Unit = {}

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
    cleanup: TestScope.() -> Unit = defaultCleanup
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
    cleanup: TestScope.() -> Unit = defaultCleanup
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
fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = defaultCleanup
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
fun <EV : Any, ST : Any, EF : Any, T : Processor<EV, ST, EF>> TestScope.processorTest(
    processor: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = defaultCleanup
) {
    launch { given() }
    runCurrent()
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
