package com.tomczyn.ellipse.test

import com.tomczyn.ellipse.Ellipse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
internal fun <T : Ellipse<EV, ST, EF>, EV : Any, ST : Any, EF : Any> TestScope.getStatesAndEffects(
    ellipseFactory: TestScope.() -> T,
    events: List<EV>,
    afterPrepare: TestScope.() -> Unit = { advanceUntilIdle() },
    afterEvents: TestScope.() -> Unit = { advanceUntilIdle() }
): Pair<TestResult<ST>, TestResult<EF>> {
    val statesList = mutableListOf<ST>()
    val effectsList = mutableListOf<EF>()
    val ellipse = ellipseFactory()
    val stateJob = launch { ellipse.state.toList(statesList) }
    val effectJob = launch { ellipse.effect.toList(effectsList) }
    afterPrepare()
    events.forEach { event -> ellipse.sendEvent(event) }
    afterEvents()
    stateJob.cancel()
    effectJob.cancel()
    return TestResultImpl(statesList) to TestResultImpl(effectsList)
}

@ExperimentalCoroutinesApi
internal fun <T : Ellipse<EV, ST, EF>, EV : Any, ST : Any, EF : Any> TestScope.getEffects(
    ellipseFactory: TestScope.() -> T,
    events: List<EV>,
    afterPrepare: TestScope.() -> Unit = { advanceUntilIdle() },
    afterEvents: TestScope.() -> Unit = { advanceUntilIdle() }
): TestResult<EF> {
    val effectsList = mutableListOf<EF>()
    val ellipse = ellipseFactory()
    val effectJob = launch { ellipse.effect.toList(effectsList) }
    afterPrepare()
    events.forEach { event -> ellipse.sendEvent(event) }
    afterEvents()
    effectJob.cancel()
    return TestResultImpl(effectsList)
}
