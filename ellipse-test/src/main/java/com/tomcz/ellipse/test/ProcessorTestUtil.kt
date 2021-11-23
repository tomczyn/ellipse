package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope

internal suspend fun <T : Processor<EV, ST, EF>, EV : Any, ST : Any, EF : Any> processTestData(
    processorFactory: () -> T,
    events: List<EV>,
    processorScope: TestCoroutineScope
): Pair<ListTester<ST>, ListTester<EF>> {
    val internalScope = TestCoroutineScope()
    processorScope.pauseDispatcher()
    val processor = processorFactory()
    val statesList = mutableListOf<ST>()
    val stateJob = internalScope.launch { processor.state.toList(statesList) }
    val effectsList = mutableListOf<EF>()
    val effectJob = internalScope.launch { processor.effect.toList(effectsList) }
    processorScope.resumeDispatcher()
    events.forEach { event -> processor.sendEvent(event) }
    processorScope.advanceUntilIdle()
    stateJob.cancelAndJoin()
    effectJob.cancelAndJoin()
    return ListTester(statesList) to ListTester(effectsList)
}
