package com.tomcz.mvi

interface PartialState<T> {
    fun reduce(oldState: T): T

    class NoAction<T> : PartialState<T> {
        override fun reduce(oldState: T): T = oldState
    }
}
