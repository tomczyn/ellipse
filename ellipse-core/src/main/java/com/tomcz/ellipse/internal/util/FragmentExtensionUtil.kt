package com.tomcz.ellipse.internal.util

import com.tomcz.ellipse.Processor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal fun <EV : Any, ST : Any, EF : Any> CoroutineScope.consume(
    processor: Processor<EV, ST, EF>,
    render: (ST) -> Unit,
    trigger: (EF) -> Unit,
    viewEvents: List<Flow<EV>> = emptyList()
) {
    launch { processor.onState(render) }
    launch { processor.onEffect(trigger) }
    launch { processor.process(viewEvents) }
}

internal fun <EV : Any, EF : Any> CoroutineScope.consume(
    processor: Processor<EV, Nothing, EF>,
    trigger: (EF) -> Unit,
    viewEvents: List<Flow<EV>> = emptyList()
) {
    launch { processor.onEffect(trigger) }
    launch { processor.process(viewEvents) }
}

internal suspend fun <EV : Any, ST : Any, EF : Any> Processor<EV, ST, EF>.process(
    viewEvents: List<Flow<EV>> = emptyList()
) = viewEvents.mergeEvents().collect { event -> sendEvent(event) }

internal suspend fun <EV : Any, ST : Any, EF : Any> Processor<EV, ST, EF>.onState(
    render: (ST) -> Unit
) = state.collect { state -> render(state) }

internal suspend fun <EV : Any, ST : Any, EF : Any> Processor<EV, ST, EF>.onEffect(
    trigger: (EF) -> Unit
) = effect.collect { effect -> trigger(effect) }
