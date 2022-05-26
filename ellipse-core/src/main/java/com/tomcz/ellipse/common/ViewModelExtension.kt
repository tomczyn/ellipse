package com.tomcz.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.PartialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

inline fun <EV : Any, reified EF : Any> ViewModel.processor(
    noinline prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    noinline onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Processor<EV, Unit, EF> = viewModelScope.processor(
    initialState = Unit,
    prepare = { prepare(); emptyFlow() },
    onEvent = { onEvent(it); emptyFlow() }
)

inline fun <EV : Any, ST : Any, PA : PartialState<ST>, reified EF : Any> ViewModel.processor(
    initialState: ST,
    noinline prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA> = { emptyFlow() },
    noinline onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Processor<EV, ST, EF> = viewModelScope.processor(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)
