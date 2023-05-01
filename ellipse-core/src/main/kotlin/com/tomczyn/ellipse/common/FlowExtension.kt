package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.PartialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@JvmName("toNoActionAny")
fun <T : Any> Flow<*>.toNoAction(): Flow<PartialState<T>> = map { NoAction() }

@JvmName("toNoActionPartial")
fun <T : Any> Flow<PartialState<T>>.toNoAction(): Flow<PartialState<T>> = this

fun <T : Any> Any?.toNoAction(): Flow<PartialState<T>> = flowOf(NoAction())
