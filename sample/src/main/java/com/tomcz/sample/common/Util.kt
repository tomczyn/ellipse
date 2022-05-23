package com.tomcz.sample.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.onCancel(action: () -> Unit) = callbackFlow<Unit> {
    awaitClose { action() }
}.launchIn(this)

suspend fun <T> FlowCollector<T>.emitAbort(value: T): Nothing {
    emit(value)
    throw CancellationException("Flow aborted")
}
