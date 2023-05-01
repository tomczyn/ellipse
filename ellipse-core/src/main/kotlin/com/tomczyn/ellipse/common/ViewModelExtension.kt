package com.tomczyn.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomczyn.ellipse.PartialState
import com.tomczyn.ellipse.Processor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, EF : Any> ViewModel.processor(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Processor<EV, Unit, EF> = viewModelScope.processor(
    initialState = Unit,
    prepare = { prepare(); emptyFlow() },
    onEvent = { onEvent(it); emptyFlow() }
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.processor(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Processor<EV, ST, EF> = viewModelScope.processor(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)
