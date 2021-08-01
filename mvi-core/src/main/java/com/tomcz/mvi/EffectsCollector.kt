package com.tomcz.mvi

interface EffectsCollector<T : Any> {
    fun send(effect: T)
}
