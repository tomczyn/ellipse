package com.tomczyn.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomczyn.ellipse.PartialState
import com.tomczyn.ellipse.Ellipse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, EF : Any> ViewModel.ellipse(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Ellipse<EV, Unit, EF> = viewModelScope.ellipse(
    initialState = Unit,
    prepare = { prepare(); emptyFlow() },
    onEvent = { onEvent(it); emptyFlow() }
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.ellipse(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Ellipse<EV, ST, EF> = viewModelScope.ellipse(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)
