package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
internal fun <T : Processor<EV, ST, EF>, EV : Any, ST : Any, EF : Any> TestScope.getStatesAndEffects(
    processorFactory: () -> T,
    events: List<EV>,
): Pair<TestResult<ST>, TestResult<EF>> {
    val statesList = mutableListOf<ST>()
    val effectsList = mutableListOf<EF>()
    val processor = processorFactory()
    val stateJob = launch { processor.state.toList(statesList) }
    val effectJob = launch { processor.effect.toList(effectsList) }
    events.forEach { event -> processor.sendEvent(event) }
    advanceUntilIdle()
    launch {
        stateJob.cancelAndJoin()
        effectJob.cancelAndJoin()
    }
    advanceUntilIdle()
    return TestResultImpl(statesList) to TestResultImpl(effectsList)
}
