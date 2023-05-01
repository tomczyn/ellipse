package com.tomczyn.ellipse

interface EffectsCollector<T : Any> {
    fun send(vararg effect: T)
}
