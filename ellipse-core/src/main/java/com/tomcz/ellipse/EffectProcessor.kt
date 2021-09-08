package com.tomcz.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow

@Stable
interface EffectProcessor<in EV : Any, out EF : Any> {
    val effect: Flow<EF>
    fun sendEvent(event: EV)
}
