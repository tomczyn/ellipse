package com.tomcz.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Stable
interface Processor<in EV : Any, out ST : Any, out EF : Any> {
    val state: StateFlow<ST>
    fun sendEvent(event: EV)
    val effect: Flow<EF>
}
