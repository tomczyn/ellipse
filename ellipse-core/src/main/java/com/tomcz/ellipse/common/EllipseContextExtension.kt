package com.tomcz.ellipse.common

import com.tomcz.ellipse.EllipseContext
import com.tomcz.ellipse.Partial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

fun <ST : Any, EF : Any> EllipseContext<ST, EF>.setState(vararg partial: Partial<ST>) {
    partial.onEach { state = it.reduce(state) }
}

fun <ST : Any, EF : Any> EllipseContext<ST, EF>.setState(vararg flow: Flow<Partial<ST>>) {
    merge(*flow)
        .onEach { state = it.reduce(state) }
        .launchIn(this)
}
