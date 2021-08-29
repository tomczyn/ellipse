package com.tomcz.ellipse.internal.util

import com.tomcz.ellipse.PartialState
import kotlinx.coroutines.flow.MutableStateFlow

fun <T : PartialState<K>, K : Any> MutableStateFlow<K>.reduceAndSet(intent: T) {
    value = intent.reduce(value)
}
