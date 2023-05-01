package com.tomczyn.ellipse.test

import com.tomczyn.ellipse.Ellipse
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
fun <EV : Any, ST : Any, EF : Any, T : Ellipse<EV, ST, EF>> ellipseTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    ellipse: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = defaultCleanup
): Unit = ellipseTest(
    ellipse = ellipse,
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
fun <EV : Any, ST : Any, EF : Any, T : Ellipse<EV, ST, EF>> ellipseTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    ellipse: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvents: List<EV> = emptyList(),
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = defaultCleanup
) = runTest(context) {
    ellipseTest(
        ellipse = ellipse,
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
fun <EV : Any, ST : Any, EF : Any, T : Ellipse<EV, ST, EF>> TestScope.ellipseTest(
    ellipse: TestScope.() -> T,
    given: suspend TestScope.() -> Unit = {},
    whenEvent: EV,
    afterPrepare: TestScope.() -> Unit = defaultAfter,
    afterEvents: TestScope.() -> Unit = defaultAfter,
    thenStates: TestResultContext<ST>.() -> Unit = {},
    thenEffects: TestResultContext<EF>.() -> Unit = {},
    cleanup: TestScope.() -> Unit = defaultCleanup
): Unit = ellipseTest(
    ellipse = ellipse,
    given = given,
    whenEvents = listOf(whenEvent),
    afterPrepare = afterPrepare,
    afterEvents = afterEvents,
    thenStates = thenStates,
    thenEffects = thenEffects,
    cleanup = cleanup,
)

@ExperimentalCoroutinesApi
fun <EV : Any, ST : Any, EF : Any, T : Ellipse<EV, ST, EF>> TestScope.ellipseTest(
    ellipse: TestScope.() -> T,
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
        ellipseFactory = ellipse,
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
