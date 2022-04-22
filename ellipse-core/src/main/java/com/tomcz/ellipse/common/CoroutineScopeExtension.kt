package com.tomcz.ellipse.common

import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.FlowEffectProcessor
import com.tomcz.ellipse.internal.FlowProcessor
import com.tomcz.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

fun <EV : Any, EF : Any> CoroutineScope.processor(
    prepare: suspend EllipseContext<Nothing, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Nothing, EF>.(EV) -> Unit = {},
): Processor<EV, Nothing, EF> = FlowEffectProcessor(
    scope = this,
    prepare = prepare,
    onEvent = onEvent
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> CoroutineScope.processor(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Processor<EV, ST, EF> = FlowProcessor(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)

@FlowPreview
fun <EV : Any, ST : Any, EF : Any> CoroutineScope.onProcessor(
    processor: () -> Processor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {},
) = launch {
    consume(
        processor = processor(),
        render = onState,
        trigger = onEffect,
        viewEvents = viewEvents()
    )
}

@FlowPreview
fun <EV : Any, EF : Any> CoroutineScope.onProcessor(
    processor: () -> Processor<EV, Nothing, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {},
) = launch {
    consume(
        processor = processor(),
        trigger = onEffect,
        viewEvents = viewEvents()
    )
}
