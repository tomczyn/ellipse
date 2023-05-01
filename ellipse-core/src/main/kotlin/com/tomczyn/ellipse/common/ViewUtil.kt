package com.tomczyn.ellipse.common

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Creates a [Flow] of click events from a [View].
 *
 * This function is used to convert click events from a [View] into a reactive [Flow].
 * Each time the [View] is clicked, a [Unit] value is emitted by the [Flow].
 *
 * @return A [Flow] of click events represented by [Unit] values.
 */
fun View.clicks(): Flow<Unit> = callbackFlow {
    val listener = View.OnClickListener { trySend(Unit) }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(null) }
}
