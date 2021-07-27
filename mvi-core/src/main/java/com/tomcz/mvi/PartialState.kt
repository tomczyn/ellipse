package com.tomcz.mvi

interface PartialState<T> {
    fun reduce(oldState: T): T
}
