package com.tomczyn.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Ellipse is a generic, stable interface representing an Ellipse model.
 * It provides a mechanism to manage state and effects by sending events.
 *
 * @param EV The event type this Ellipse accepts. Must be a subtype of [Any].
 * @param ST The state type this Ellipse produces. Must be a subtype of [Any].
 * @param EF The effect type this Ellipse produces. Must be a subtype of [Any].
 */
@Stable
interface Ellipse<in EV : Any, out ST : Any, out EF : Any> {

    /**
     * A [StateFlow] representing the state of the Ellipse model.
     * It emits updates to the state whenever there is a change.
     */
    val state: StateFlow<ST>

    /**
     * A [Flow] representing the side effects produced by the Ellipse model.
     * It emits effects when they occur as a result of processing events.
     */
    val effect: Flow<EF>

    /**
     * Sends one or more events to the Ellipse model for processing.
     * This can cause state updates and/or effects to be emitted.
     *
     * @param event A vararg of events of type [EV] to send to the Ellipse model.
     */
    fun sendEvent(vararg event: EV)
}
