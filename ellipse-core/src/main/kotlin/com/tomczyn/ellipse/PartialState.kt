package com.tomczyn.ellipse

/**
 * Represents a partial update of a state in a state management system.
 *
 * This interface is used to define a partial state update that can be applied to a current state.
 * It provides a `reduce` method that takes the old state and returns a new state after applying the partial update.
 *
 * @param T The type of the state that the partial update can be applied to.
 */
interface PartialState<T : Any> {

    /**
     * Applies the partial update to the provided old state and returns the new state.
     *
     * This method should be implemented to apply a partial state update to the current state.
     * It takes the old state as an input and returns the new state after applying the update.
     *
     * @param oldState The current state of type [T] that the partial update should be applied to.
     * @return The new state of type [T] after applying the partial update.
     */
    fun reduce(oldState: T): T
}
