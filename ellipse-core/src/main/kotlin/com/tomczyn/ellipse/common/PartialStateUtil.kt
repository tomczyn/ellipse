package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.PartialState

/**
 * Returns an instance of [PartialState] that represents a no-action operation.
 *
 * This function is used to create a [PartialState] object that, when applied, doesn't modify the current state.
 * It can be useful in scenarios where you want to represent a no-operation or no-change action in your state management system.
 *
 * @param T The type of the state.
 * @return An instance of [PartialState] that doesn't modify the current state.
 */
@Suppress("FunctionName")
fun <T : Any> NoAction(): PartialState<T> = object : PartialState<T> {
    override fun reduce(oldState: T): T = oldState
}
