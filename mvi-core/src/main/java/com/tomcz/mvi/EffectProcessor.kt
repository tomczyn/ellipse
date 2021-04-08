package com.tomcz.mvi

import kotlinx.coroutines.flow.Flow

interface EffectProcessor<in EV : Any, out EF : Any> {
    val effect: Flow<EF>
    fun sendEvent(event: EV)
}
