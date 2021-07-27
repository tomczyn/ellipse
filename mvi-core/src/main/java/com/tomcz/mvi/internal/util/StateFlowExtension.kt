package com.tomcz.mvi.internal.util

import com.tomcz.mvi.PartialState
import kotlinx.coroutines.flow.MutableStateFlow

fun <T : PartialState<K>, K : Any> MutableStateFlow<K>.reduceAndSet(intent: T) {
    value = intent.reduce(value)
}
