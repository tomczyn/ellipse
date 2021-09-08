package com.tomcz.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface StateProcessor<in EV : Any, out ST : Any> {
    val state: StateFlow<ST>
    fun sendEvent(event: EV)
}
