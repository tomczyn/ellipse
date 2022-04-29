package com.tomcz.ellipse.common

import com.tomcz.ellipse.Partial

@Suppress("FunctionName")
fun <T : Any> NoAction(): Partial<T> = object : Partial<T> {
    override fun reduce(oldState: T): T = oldState
}
