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

/**
 * Creates an `Ellipse` instance with the provided `prepare` and `onEvent` functions and without state.
 *
 * @param EV The type of the event in the `Ellipse`.
 * @param EF The type of the effect in the `Ellipse`.
 * @param prepare A suspend function that is executed during the initialization of the `Ellipse`. It's used to prepare the `Ellipse` and has no access to the state. Default is an empty lambda.
 * @param onEvent A suspend function that is executed when an event is sent to the `Ellipse`. Default is an empty lambda.
 * @return An instance of `Ellipse` with the provided `prepare` and `onEvent` functions.
 */
fun <EV : Any, EF : Any> CoroutineScope.ellipse(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Ellipse<EV, Unit, EF> = ellipse(
    initialState = Unit,
    { prepare(); emptyFlow() },
    { onEvent(it); emptyFlow() }
)

/**
 * Creates an `Ellipse` instance with the provided initial state, `prepare` and `onEvent` functions.
 *
 * @param ST The type of the state in the `Ellipse`.
 * @param EV The type of the event in the `Ellipse`.
 * @param PA A `PartialState` that represents a partial update of the state.
 * @param EF The type of the effect in the `Ellipse`.
 * @param initialState The initial state of the `Ellipse`.
 * @param prepare A suspend function that is executed during the initialization of the `Ellipse`. Default is an empty lambda that returns an empty flow.
 * @param onEvent A suspend function that is executed when an event is sent to the `Ellipse`. Default is an empty lambda that returns an empty flow.
 * @return An instance of `Ellipse` with the provided initial state, `prepare` and `onEvent` functions.
 */
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

/**
 * Launches a coroutine that consumes the given `Ellipse` instance, renders its states, and triggers its effects.
 *
 * @param EV The type of the event in the `Ellipse`.
 * @param ST The type of the state in the `Ellipse`.
 * @param EF The type of the effect in the `Ellipse`.
 * @param ellipse A function that returns an instance of `Ellipse` to be consumed.
 * @param viewEvents A function that returns a list of event flows to be sent to the `Ellipse`. Default is an empty list.
 * @param onState A function that is called with the current state of the `Ellipse` each time it changes. Default is an empty lambda.
 * @param onEffect A function that is called with the current effect of the `Ellipse` each time it's emitted. Default is an empty lambda.
 */
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
