package com.tomcz.ellipse.common

import com.tomcz.ellipse.PartialState

fun <T : Any> NoAction(): PartialState<T> = object : PartialState<T> {
    override fun reduce(oldState: T): T = oldState
}
