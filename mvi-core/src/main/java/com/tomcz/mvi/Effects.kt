package com.tomcz.mvi

interface Effects<T : Any> {
    fun send(effect: T)
}
