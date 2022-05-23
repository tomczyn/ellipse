package com.tomcz.ellipse.common

import com.tomcz.ellipse.EllipseContext
import com.tomcz.ellipse.Partial
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.FlowProcessor
import com.tomcz.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <EV : Any, EF : Any> CoroutineScope.processor(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Processor<EV, Unit, EF> = processor(
    initialState = Unit,
    { prepare() },
    { onEvent(it) }
)

fun <EV : Any, ST : Any, PA : Partial<ST>, EF : Any> CoroutineScope.processor(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Unit = {},
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
