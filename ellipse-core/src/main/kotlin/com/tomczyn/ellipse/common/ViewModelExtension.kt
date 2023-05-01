package com.tomczyn.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomczyn.ellipse.PartialState
import com.tomczyn.ellipse.Ellipse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Creates an `Ellipse` instance with the provided `prepare` and `onEvent` functions and without state within a [ViewModel].
 *
 * @param EV The type of the event in the `Ellipse`.
 * @param EF The type of the effect in the `Ellipse`.
 * @param prepare A suspend function that is executed during the initialization of the `Ellipse`. It's used to prepare the `Ellipse` and has no access to the state. Default is an empty lambda.
 * @param onEvent A suspend function that is executed when an event is sent to the `Ellipse`. Default is an empty lambda.
 * @return An instance of `Ellipse` with the provided `prepare` and `onEvent` functions.
 */
fun <EV : Any, EF : Any> ViewModel.ellipse(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Ellipse<EV, Unit, EF> = viewModelScope.ellipse(
    initialState = Unit,
    prepare = { prepare(); emptyFlow() },
    onEvent = { onEvent(it); emptyFlow() }
)


/**
 * Creates an `Ellipse` instance with the provided initial state, `prepare` and `onEvent` functions within a [ViewModel].
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
fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.ellipse(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Ellipse<EV, ST, EF> = viewModelScope.ellipse(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)
