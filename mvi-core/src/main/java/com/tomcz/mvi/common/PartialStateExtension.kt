package com.tomcz.mvi.common

import com.tomcz.mvi.PartialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T : Any> NoAction(): PartialState<T> = object : PartialState<T> {
    override fun reduce(oldState: T): T = oldState
}

fun <T : Any> Any.thenNoAction(): Flow<PartialState<T>> = flowOf(NoAction())
