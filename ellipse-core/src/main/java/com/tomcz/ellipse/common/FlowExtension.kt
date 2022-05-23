package com.tomcz.ellipse.common

import com.tomcz.ellipse.Partial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@JvmName("toNoActionAny")
fun <T : Any> Flow<*>.toNoAction(): Flow<Partial<T>> = map { NoAction() }

@JvmName("toNoActionPartial")
fun <T : Any> Flow<Partial<T>>.toNoAction(): Flow<Partial<T>> = this

fun <T : Any> Any?.toNoAction(): Flow<Partial<T>> = flowOf(NoAction())
