package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.PartialState
import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.internal.EllipseImpl
import com.tomczyn.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

fun <EV : Any, EF : Any> CoroutineScope.ellipse(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Ellipse<EV, Unit, EF> = ellipse(
    initialState = Unit,
    { prepare(); emptyFlow() },
    { onEvent(it); emptyFlow() }
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> CoroutineScope.ellipse(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Ellipse<EV, ST, EF> = EllipseImpl(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)

@FlowPreview
fun <EV : Any, ST : Any, EF : Any> CoroutineScope.onEllipse(
    ellipse: () -> Ellipse<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {},
) = launch {
    consume(
        ellipse = ellipse(),
        render = onState,
        trigger = onEffect,
        viewEvents = viewEvents()
    )
}
