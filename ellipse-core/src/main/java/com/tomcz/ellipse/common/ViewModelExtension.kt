package com.tomcz.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, EF : Any> ViewModel.processor(
    prepare: suspend EllipseContext<Nothing, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Nothing, EF>.(EV) -> Unit = {},
): Processor<EV, Nothing, EF> = viewModelScope.processor(
    prepare = prepare,
    onEvent = onEvent
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
