package com.tomcz.sample.util

import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.common.NoAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Extension function to conveniently return flow of
 * NoAction PartialState for any processor. It's not in the library itself,
 * because not everyone would like to have an extension function on Any type.
 */
fun <T : Any> Any.thenNoAction(): Flow<PartialState<T>> = flowOf(NoAction())
