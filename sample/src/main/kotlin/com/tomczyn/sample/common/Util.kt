package com.tomczyn.sample.common

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.cancellation.CancellationException

fun <T> onCancel(action: () -> Unit) = callbackFlow<T> {
    awaitClose { action() }
}

suspend fun <T> FlowCollector<T>.emitAbort(value: T): Nothing {
    emit(value)
    throw CancellationException("Flow aborted")
}
