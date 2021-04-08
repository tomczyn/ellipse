package com.tomcz.mvi.test

import com.tomcz.mvi.StateEffectProcessor
import kotlinx.coroutines.CoroutineScope

class StateEffectProcessorTest<E : Any, S : Any, EF : Any, T : StateEffectProcessor<E, S, EF>>(
    private val processor: T,
    events: List<E>,
    scope: CoroutineScope
) {

    val states: ListTester<S>
        get() = ListTester(_states.values)
    private val _states: FlowTester<S> = FlowTester(scope, processor.state)

    val effects: ListTester<EF>
        get() = ListTester(_effects.values)
    private val _effects: FlowTester<EF> = FlowTester(scope, processor.effect)

    init {
        events.forEach { event -> processor.sendEvent(event) }
        _states.finish()
        _effects.finish()
    }
}
