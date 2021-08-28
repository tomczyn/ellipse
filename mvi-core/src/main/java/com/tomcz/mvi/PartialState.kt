package com.tomcz.mvi

interface PartialState<T : Any> {
    fun reduce(oldState: T): T
}
