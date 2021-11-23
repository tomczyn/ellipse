package com.tomcz.ellipse

interface EffectsCollector<T : Any> {
    fun sendEffect(vararg effect: T)
}
