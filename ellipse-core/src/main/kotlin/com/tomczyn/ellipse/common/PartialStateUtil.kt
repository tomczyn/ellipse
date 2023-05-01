package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.PartialState

@Suppress("FunctionName")
fun <T : Any> NoAction(): PartialState<T> = object : PartialState<T> {
    override fun reduce(oldState: T): T = oldState
}
