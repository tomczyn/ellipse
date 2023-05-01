package com.tomczyn.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents an `Ellipse`, a state management system with event handling and effects.
 *
 * This interface defines an `Ellipse` that can handle events, manage state, and emit effects.
 * It exposes the current state as a [StateFlow], effects as a [Flow], and provides a `sendEvent` method to send events.
 *
 * @param EV The type of the event that the `Ellipse` can handle.
 * @param ST The type of the state managed by the `Ellipse`.
 * @param EF The type of the effect emitted by the `Ellipse`.
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
