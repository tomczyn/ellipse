package com.tomcz.ellipse.common

import com.tomcz.ellipse.PartialState

@Suppress("FunctionName")
fun <T : Any> NoAction(): PartialState<T> = object : PartialState<T> {
    override fun reduce(oldState: T): T = oldState
}
