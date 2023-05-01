package com.tomczyn.ellipse.internal.util

import com.tomczyn.ellipse.PartialState
import kotlinx.coroutines.flow.MutableStateFlow

fun <T : PartialState<K>, K : Any> MutableStateFlow<K>.reduceAndSet(intent: T) {
    value = intent.reduce(value)
}
