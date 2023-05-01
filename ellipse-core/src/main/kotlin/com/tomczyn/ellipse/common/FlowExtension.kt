package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.PartialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Transforms a [Flow] of any type to a [Flow] of [PartialState] with [NoAction] items.
 *
 * @param T The type of the state.
 * @return A [Flow] of [PartialState] with [NoAction] items.
 */
@JvmName("toNoActionAny")
fun <T : Any> Flow<*>.toNoAction(): Flow<PartialState<T>> = map { NoAction() }

/**
 * Returns the same [Flow] of [PartialState] for the given type.
 *
 * This function is provided to avoid ambiguity when working with [Flow]s of [PartialState].
 *
 * @param T The type of the state.
 * @return The same [Flow] of [PartialState] for the given type.
 */
@JvmName("toNoActionPartial")
fun <T : Any> Flow<PartialState<T>>.toNoAction(): Flow<PartialState<T>> = this


/**
 * Creates a [Flow] of [PartialState] containing a single [NoAction] item for the given type.
 *
 * @param T The type of the state.
 * @return A [Flow] of [PartialState] containing a single [NoAction] item.
 */
fun <T : Any> Any?.toNoAction(): Flow<PartialState<T>> = flowOf(NoAction())
