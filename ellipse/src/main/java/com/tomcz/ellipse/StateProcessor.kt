package com.tomcz.ellipse

import kotlinx.coroutines.flow.StateFlow

interface StateProcessor<in EV : Any, out ST : Any> {
    val state: StateFlow<ST>
    fun sendEvent(event: EV)
}
