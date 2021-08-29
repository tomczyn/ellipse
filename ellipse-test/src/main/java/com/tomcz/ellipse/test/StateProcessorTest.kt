package com.tomcz.ellipse.test

import com.tomcz.ellipse.StateProcessor
import kotlinx.coroutines.CoroutineScope

class StateProcessorTest<E : Any, S : Any, T : StateProcessor<E, S>>(
    private val processor: T,
    events: List<E>,
    scope: CoroutineScope
) {

    val states: ListTester<S>
        get() = ListTester(_states.values)
    private val _states: FlowTester<S> = FlowTester(scope, processor.state)

    init {
        events.forEach { event -> processor.sendEvent(event) }
        _states.finish()
    }
}
