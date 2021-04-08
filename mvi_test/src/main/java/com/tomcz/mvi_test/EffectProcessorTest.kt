package com.tomcz.mvi_test

import com.tomcz.mvi.EffectProcessor
import kotlinx.coroutines.CoroutineScope

class EffectProcessorTest<EV : Any, EF : Any, T : EffectProcessor<EV, EF>>(
    private val processor: T,
    events: List<EV>,
    scope: CoroutineScope
) {

    val effects: ListTester<EF>
        get() = ListTester(_states.values)
    private val _states: FlowTester<EF> = FlowTester(scope, processor.effect)

    init {
        events.forEach { event -> processor.sendEvent(event) }
        _states.finish()
    }
}
