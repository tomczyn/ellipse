package com.tomcz.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.ellipse.EllipseContext
import com.tomcz.ellipse.Processor

fun <EV : Any, EF : Any> ViewModel.processor(
    prepare: suspend EllipseContext<Unit, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<Unit, EF>.(EV) -> Unit = {},
): Processor<EV, Unit, EF> = viewModelScope.processor(
    initialState = Unit,
    prepare = { prepare() },
    onEvent = { onEvent(it) }
)

fun <EV : Any, ST : Any, EF : Any> ViewModel.processor(
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Unit = {},
    onEvent: suspend EllipseContext<ST, EF>.(EV) -> Unit = {},
): Processor<EV, ST, EF> = viewModelScope.processor(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)
