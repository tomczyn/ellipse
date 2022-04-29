package com.tomcz.ellipse.internal.util

import com.tomcz.ellipse.Partial
import kotlinx.coroutines.flow.MutableStateFlow

fun <T : Partial<K>, K : Any> MutableStateFlow<K>.reduceAndSet(intent: T) {
    value = intent.reduce(value)
}
