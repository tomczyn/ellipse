package com.tomcz.mvi.internal.util

import com.tomcz.mvi.Intent
import kotlinx.coroutines.flow.MutableStateFlow

fun <T : Intent<K>, K : Any> MutableStateFlow<K>.reduceAndSet(intent: T) {
    value = intent.reduce(value)
}
