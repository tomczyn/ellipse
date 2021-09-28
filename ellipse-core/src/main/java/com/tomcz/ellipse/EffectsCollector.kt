package com.tomcz.ellipse

interface EffectsCollector<T : Any> {
    fun sendEffect(effect: T)
}
