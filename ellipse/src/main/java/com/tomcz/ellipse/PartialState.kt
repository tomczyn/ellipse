package com.tomcz.ellipse

interface PartialState<T : Any> {
    fun reduce(oldState: T): T
}
