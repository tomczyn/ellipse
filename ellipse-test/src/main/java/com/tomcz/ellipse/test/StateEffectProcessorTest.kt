package com.tomcz.ellipse.test

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope

class StateEffectProcessorTest<E : Any, S : Any, EF : Any, T : Processor<E, S, EF>>(
    processorFactory: () -> T,
    events: List<E>,
    processorScope: TestCoroutineScope
) {

    val states: ListTester<S>
    val effects: ListTester<EF>

    private val internalScope = TestCoroutineScope()

    init {
        processorScope.pauseDispatcher()

        val processor = processorFactory()

        val statesList = mutableListOf<S>()
        val stateJob = internalScope.launch { processor.state.toList(statesList) }

        val effectsList = mutableListOf<EF>()
        val effectJob = internalScope.launch { processor.effect.toList(effectsList) }

        processorScope.resumeDispatcher()

        events.forEach { event -> processor.sendEvent(event) }

        stateJob.cancel()
        effectJob.cancel()

        states = ListTester(statesList)
        effects = ListTester(effectsList)
    }
}
