package com.tomcz.mvi

interface PartialState<T : Any> {
    fun reduce(oldState: T): T

    class NoAction<T : Any> : PartialState<T> {
        override fun reduce(oldState: T): T = oldState
    }
}
