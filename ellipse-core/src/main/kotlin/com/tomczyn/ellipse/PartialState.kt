package com.tomczyn.ellipse

interface PartialState<T : Any> {
    fun reduce(oldState: T): T
}
