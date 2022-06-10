package com.tomcz.ellipse.common

import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.FlowProcessor
import com.tomcz.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

inline fun <EV : Any, reified EF : Any> CoroutineScope.processor(
    noinline prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    noinline onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Processor<EV, Unit, EF> = processor(
    initialState = Unit,
    { prepare(); emptyFlow() },
    { onEvent(it); emptyFlow() }
)

inline fun <EV : Any, ST : Any, PA : PartialState<ST>, reified EF : Any> CoroutineScope.processor(
    initialState: ST,
    noinline prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    noinline onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Processor<EV, ST, EF> = FlowProcessor(
    scope = this,
    effectClass = EF::class,
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
