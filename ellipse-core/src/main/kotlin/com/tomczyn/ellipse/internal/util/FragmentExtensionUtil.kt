package com.tomczyn.ellipse.internal.util

import com.tomczyn.ellipse.Ellipse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@FlowPreview
internal fun <EV : Any, ST : Any, EF : Any> CoroutineScope.consume(
    ellipse: Ellipse<EV, ST, EF>,
    viewEvents: List<Flow<EV>> = emptyList(),
    render: (ST) -> Unit,
    trigger: (EF) -> Unit
) {
    launch { ellipse.onState(render) }
    launch { ellipse.onEffect(trigger) }
    launch { ellipse.process(viewEvents) }
}

@FlowPreview
internal suspend fun <EV : Any, ST : Any, EF : Any> Ellipse<EV, ST, EF>.process(
    viewEvents: List<Flow<EV>> = emptyList()
) = viewEvents.mergeEvents().collect { event -> sendEvent(event) }

internal suspend fun <EV : Any, ST : Any, EF : Any> Ellipse<EV, ST, EF>.onState(
    render: (ST) -> Unit
): Nothing = state.collect { state -> render(state) }

internal suspend fun <EV : Any, ST : Any, EF : Any> Ellipse<EV, ST, EF>.onEffect(
    trigger: (EF) -> Unit
) = effect.collect { effect -> trigger(effect) }
