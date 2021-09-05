package com.tomcz.ellipse

interface EffectsCollector<T : Any> {
    fun send(effect: T)
}
