package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.EffectsCollector
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the context of an `Ellipse` instance, providing access to its state and effects.
 *
 * The `EllipseContext` class is used within the `prepare` and `onEvent` functions of an `Ellipse` instance.
 * It provides access to the `state` and `effects` of the `Ellipse`, allowing users to read the current state
 * and emit effects during the execution of the `prepare` and `onEvent` functions.
 *
 * @param ST The type of the state in the `Ellipse`.
 * @param EF The type of the effect in the `Ellipse`.
 * @property state The state of the `Ellipse` as a `StateFlow`.
 * @property effects An instance of `EffectsCollector` used to emit effects within the `Ellipse`.
 */
class EllipseContext<ST : Any, EF : Any>(
    val state: StateFlow<ST>,
    val effects: EffectsCollector<EF>
)
