package com.tomcz.ellipse

interface EffectsCollector<T : Any> {
    fun send(vararg effect: T)
}
