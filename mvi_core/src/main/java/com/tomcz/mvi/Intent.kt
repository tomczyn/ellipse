package com.tomcz.mvi

interface Intent<T> {
    fun reduce(oldState: T): T
}
