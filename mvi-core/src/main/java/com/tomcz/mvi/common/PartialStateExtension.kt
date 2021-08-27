package com.tomcz.mvi.common

import com.tomcz.mvi.PartialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T : Any> Any.thenNoAction(): Flow<PartialState<T>> = flowOf(PartialState.NoAction())
