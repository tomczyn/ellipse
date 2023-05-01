package com.tomczyn.ellipse.common

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Extends [AppCompatActivity] to work with an [Ellipse] model.
 *
 * @param lifecycleState The [Lifecycle.State] in which the coroutine should run.
 * @param ellipse The [Ellipse] model to use.
 * @param viewEvents An optional lambda returning a list of [Flow]s representing view events to be sent to the Ellipse model.
 * @param onState An optional lambda to be called with the new state when the state updates.
 * @param onEffect An optional lambda to be called with the effect when an effect is emitted.
 */
@FlowPreview
fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onEllipse(
    lifecycleState: Lifecycle.State,
    ellipse: () -> Ellipse<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(ellipse(), viewEvents(), onState, onEffect) }

/**
 * Extends [ComponentActivity] to work with an [Ellipse] model.
 *
 * @param lifecycleState The [Lifecycle.State] in which the coroutine should run.
 * @param ellipse The [Ellipse] model to use.
 * @param viewEvents An optional lambda returning a list of [Flow]s representing view events to be sent to the Ellipse model.
 * @param onState An optional lambda to be called with the new state when the state updates.
 * @param onEffect An optional lambda to be called with the effect when an effect is emitted.
 */
@FlowPreview
fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onEllipse(
    lifecycleState: Lifecycle.State,
    ellipse: () -> Ellipse<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(ellipse(), viewEvents(), onState, onEffect) }

private fun AppCompatActivity.launch(
    lifecycleState: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(lifecycleState) { action() }
}

private fun ComponentActivity.launch(
    lifecycleState: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(lifecycleState) { action() }
}
